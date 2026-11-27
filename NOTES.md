# Project Notes

- The initial implementation targets ECS Fargate, an Application Load Balancer, and RDS PostgreSQL.
- Terraform resources intentionally use private subnets for application and database tiers.
- H2 is used only for local development and tests; PostgreSQL is used in AWS.