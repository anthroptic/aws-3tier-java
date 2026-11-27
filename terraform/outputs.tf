output "aws_region" {
  description = "AWS region"
  value       = var.aws_region
}

output "ecr_repository_url" {
  description = "ECR repository URL"
  value       = aws_ecr_repository.app.repository_url
}

output "application_url" {
  description = "Public application URL"
  value       = "http://${aws_lb.app.dns_name}"
}
