# Email Lambda Function

## Overview
The **Email Lambda Function** is part of the **AWS Acquaintance** project. It is responsible for:
- Sending emails to users after:
  - File upload completion (triggered by the **Image Service**).
  - File deletion notification (triggered by the Step Function).

## Features
- Integration with AWS SES (Simple Email Service) for email notifications.
- Subscribed to AWS SNS events for upload and deletion notifications.



