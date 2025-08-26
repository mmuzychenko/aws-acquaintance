# AWS Acquaintance Project

A microservices-based project demonstrating best practices for building cloud-native applications on AWS using Terraform, Spring Boot, and various AWS-managed services.

## 📦 Services

### 1. **User Service**
- Handles user registration and authentication.
- Issues JWT tokens.
- Uses Spring Security.
- Triggers AWS Step Functions on image upload events.

### 2. **Image Service**
- Accepts file uploads.
- Stores uploaded files in an **S3 bucket**.
- Saves metadata (uploader, timestamp, etc.) in **Amazon DocumentDB (MongoDB)**.
- Publishes **SNS events** after upload completion.

## 🗄️ Databases

- **PostgreSQL (RDS)** – for user data (used by User Service).
- **DocumentDB (MongoDB)** – for image metadata (used by Image Service).

## ☁️ AWS Services Used

| Service | Purpose |
|--------|---------|
| **EC2** | Hosts the User and Image Spring Boot services |
| **S3** | Stores uploaded images |
| **RDS (PostgreSQL)** | Stores user data |
| **DocumentDB (MongoDB)** | Stores image metadata |
| **SNS** | Publishes events after image upload or deletion |
| **SQS (optional)** | For decoupled event processing |
| **Lambda** | Sends email notifications on image upload and deletion |
| **Step Functions** | Orchestrates async workflows (e.g., delayed file deletion) |
| **Secrets Manager** | Stores DB passwords and other secrets |
| **Terraform** | Provisions all infrastructure as code |

## 🔄 Workflow Overview

1. **User uploads file** → Image Service saves it to **S3** and metadata to **DocumentDB**.
2. **Image Service** publishes an **SNS event** (file uploaded).
3. **User Service** and a **Lambda function** subscribe to this event.
4. **Lambda** sends a "File uploaded" email notification.
5. **User Service** triggers a **Step Function** that:
    - Waits for 10 minutes
    - Sends delete request to **Image Service**
6. **Image Service** deletes file from S3 and metadata from MongoDB, then publishes a new SNS event.
7. **Lambda** sends a final email: `"File {file_name} is deleted"`.

## 🚀 Deployment

All services and infrastructure are deployed using **Terraform**.

- Resources: EC2, S3, RDS, DocumentDB, IAM roles, SNS, Lambda, Step Functions
- Secrets (DB credentials, tokens) are managed using **AWS Secrets Manager**

## 📁 Project Structure

