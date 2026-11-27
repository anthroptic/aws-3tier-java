variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "project_name" {
  description = "Name prefix for AWS resources"
  type        = string
  default     = "aws-3tier-java"
}

variable "app_image_tag" {
  description = "Application image tag in the ECR repository"
  type        = string
  default     = "latest"
}

variable "app_count" {
  description = "Number of ECS tasks"
  type        = number
  default     = 0
}

variable "db_name" {
  description = "PostgreSQL database name"
  type        = string
  default     = "appdb"
}

variable "db_username" {
  description = "PostgreSQL master username"
  type        = string
  default     = "appuser"
}
