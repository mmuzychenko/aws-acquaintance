provider "aws" {
  region = "us-east-1"
}

resource "aws_s3_bucket" "file_upload_bucket" {
  bucket = "myapp-file-uploads"
  force_destroy = true
}

resource "aws_security_group" "ec2_sg" {
  name        = "ec2_sg"
  description = "Allow inbound HTTP/SSH"
  vpc_id      = "<your-vpc-id>"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "spring_app" {
  ami                    = "<your-ami-id>"
  instance_type          = "t3.micro"
  vpc_security_group_ids = [aws_security_group.ec2_sg.id]
  key_name               = "<your-key-name>"

  tags = {
    Name = "spring-microservice"
  }
}

resource "aws_docdb_cluster" "docdb" {
  cluster_identifier      = "docdb-cluster"
  engine                  = "docdb"
  master_username         = "admin"
  master_password         = "yourpassword"
  skip_final_snapshot     = true
}

resource "aws_iam_role" "ec2_role" {
  name = "ec2_app_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Effect = "Allow"
      Principal = {
        Service = "ec2.amazonaws.com"
      }
      Action = "sts:AssumeRole"
    }]
  })
}
