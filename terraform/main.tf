provider "aws" {
  region = var.region
}

# VPC
resource "aws_vpc" "main" {
  cidr_block = "10.0.0.0/16"
  enable_dns_support = true
  enable_dns_hostnames = true
  tags = { Name = "aws-acquaintance-vpc" }
}

# Subnets
resource "aws_subnet" "public" {
  vpc_id = aws_vpc.main.id
  cidr_block = "10.0.1.0/24"
  availability_zone = "${var.region}a"
  map_public_ip_on_launch = true
  tags = { Name = "public-subnet" }
}

# Internet Gateway
resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.main.id
  tags = { Name = "aws-acquaintance-igw" }
}

# Route Table
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.main.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }
  tags = { Name = "public-route-table" }
}

resource "aws_route_table_association" "public" {
  subnet_id = aws_subnet.public.id
  route_table_id = aws_route_table.public.id
}

# Secrets Manager
resource "aws_secretsmanager_secret" "user_service_secret" {
  name = "user-service-secrets"
}

resource "aws_secretsmanager_secret" "image_service_secret" {
  name = "image-service-secrets"
}

# RDS Postgres
resource "aws_db_instance" "postgres" {
  identifier = "user-db"
  engine = "postgres"
  engine_version = "15.3"
  instance_class = "db.t3.micro"
  allocated_storage = 20
  username = "admin"
  password = jsondecode(aws_secretsmanager_secret_version.user_service_secret_version.secret_string)["password"]
  vpc_security_group_ids = [aws_security_group.db_sg.id]
  db_subnet_group_name = aws_db_subnet_group.main.name
  skip_final_snapshot = true
}

resource "aws_db_subnet_group" "main" {
  name = "main"
  subnet_ids = [aws_subnet.public.id]
}

# DocumentDB
resource "aws_docdb_cluster" "docdb" {
  cluster_identifier = "image-metadata"
  engine = "docdb"
  master_username = "admin"
  master_password = jsondecode(aws_secretsmanager_secret_version.image_service_secret_version.secret_string)["password"]
  vpc_security_group_ids = [aws_security_group.db_sg.id]
  db_subnet_group_name = aws_db_subnet_group.main.name
  skip_final_snapshot = true
}

# S3 Bucket
resource "aws_s3_bucket" "image_bucket" {
  bucket = "image-bucket-${var.environment}"
}

# SNS Topic
resource "aws_sns_topic" "image_events" {
  name = "image-events"
}

resource "aws_sns_topic_subscription" "lambda_upload" {
  topic_arn = aws_sns_topic.image_events.arn
  protocol = "lambda"
  endpoint = aws_lambda_function.notify_upload.arn
}

resource "aws_sns_topic_subscription" "lambda_delete" {
  topic_arn = aws_sns_topic.image_events.arn
  protocol = "lambda"
  endpoint = aws_lambda_function.notify_delete.arn
}

# EC2 Instances
resource "aws_instance" "user_service" {
  ami = "ami-0c55b159cbfafe1f0" # Amazon Linux 2
  instance_type = "t2.micro"
  subnet_id = aws_subnet.public.id
  vpc_security_group_ids = [aws_security_group.app_sg.id]
  user_data = <<-EOF
              #!/bin/bash
              yum update -y
              amazon-linux-extras install java-11-openjdk -y
              curl -o user-service.jar <YOUR_USER_SERVICE_ARTIFACT_URL>
              java -jar user-service.jar
              EOF
  tags = { Name = "user-service" }
}

resource "aws_instance" "image_service" {
  ami = "ami-0c55b159cbfafe1f0"
  instance_type = "t2.micro"
  subnet_id = aws_subnet.public.id
  vpc_security_group_ids = [aws_security_group.app_sg.id]
  user_data = <<-EOF
              #!/bin/bash
              yum update -y
              amazon-linux-extras install java-11-openjdk -y
              curl -o image-service.jar <YOUR_IMAGE_SERVICE_ARTIFACT_URL>
              java -jar image-service.jar
              EOF
  tags = { Name = "image-service" }
}

# Security Groups
resource "aws_security_group" "app_sg" {
  name = "app-sg"
  vpc_id = aws_vpc.main.id
  ingress {
    from_port = 8080
    to_port = 8080
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port = 8081
    to_port = 8081
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "db_sg" {
  name = "db-sg"
  vpc_id = aws_vpc.main.id
  ingress {
    from_port = 5432
    to_port = 5432
    protocol = "tcp"
    security_groups = [aws_security_group.app_sg.id]
  }
  ingress {
    from_port = 27017
    to_port = 27017
    protocol = "tcp"
    security_groups = [aws_security_group.app_sg.id]
  }
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Step Function
resource "aws_sfn_state_machine" "delete_file_workflow" {
  name = "delete-file-workflow"
  role_arn = aws_iam_role.sfn_role.arn
  definition = jsonencode({
    Comment = "File Deletion Workflow",
    StartAt = "Wait10Minutes",
    States = {
      Wait10Minutes = {
        Type = "Wait",
        Seconds = 600,
        Next = "DeleteFile"
      },
      DeleteFile = {
        Type = "Task",
        Resource = "arn:aws:states:::http:invoke",
        Parameters = {
          ApiEndpoint = "${aws_instance.image_service.public_dns}:8081/api/images/$.fileName",
          Method = "DELETE",
          Authentication = { Type = "NO_AUTH" },
          Payload = { "fileName.$" = "$.fileName" }
        },
        End = true
      }
    }
  })
}

# IAM Roles
resource "aws_iam_role" "sfn_role" {
  name = "step-function-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Action = "sts:AssumeRole",
      Effect = "Allow",
      Principal = { Service = "states.amazonaws.com" }
    }]
  })
}

resource "aws_iam_role_policy" "sfn_policy" {
  role = aws_iam_role.sfn_role.id
  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Action = ["lambda:InvokeFunction", "states:StartExecution"],
        Resource = "*"
      }
    ]
  })
}

resource "aws_iam_role" "lambda_role" {
  name = "lambda-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Action = "sts:AssumeRole",
      Effect = "Allow",
      Principal = { Service = "lambda.amazonaws.com" }
    }]
  })
}

resource "aws_iam_role_policy" "lambda_policy" {
  role = aws_iam_role.lambda_role.id
  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect = "Allow",
        Action = ["logs:CreateLogGroup", "logs:CreateLogStream", "logs:PutLogEvents", "ses:SendEmail"],
        Resource = "*"
      }
    ]
  })
}

# Lambda Functions
resource "aws_lambda_function" "notify_upload" {
  filename = "lambda/notify_upload.zip"
  function_name = "notifyUpload"
  role = aws_iam_role.lambda_role.arn
  handler = "index.handler"
  runtime = "nodejs16.x"
  environment {
    variables = { REGION = var.region }
  }
}

resource "aws_lambda_function" "notify_delete" {
  filename = "lambda/notify_delete.zip"
  function_name = "notifyDelete"
  role = aws_iam_role.lambda_role.arn
  handler = "index.handler"
  runtime = "nodejs16.x"
  environment {
    variables = { REGION = var.region }
  }
}

# Secrets Manager Versions
resource "aws_secretsmanager_secret_version" "user_service_secret_version" {
  secret_id = aws_secretsmanager_secret.user_service_secret.id
  secret_string = jsonencode({
    password = "your-secure-db-password"
  })
}

resource "aws_secretsmanager_secret_version" "image_service_secret_version" {
  secret_id = aws_secretsmanager_secret.image_service_secret.id
  secret_string = jsonencode({
    password = "your-secure-mongo-password"
  })
}