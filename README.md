# AWS 3-Tier Java Deployment

A deployable three-tier Java web application:

- **Presentation:** Internet-facing Application Load Balancer
- **Application:** Spring Boot service running on ECS Fargate
- **Data:** PostgreSQL on Amazon RDS

The application is containerized and the AWS resources are provisioned with Terraform.

## Architecture

```text
Internet -> ALB -> ECS Fargate (private subnets) -> RDS PostgreSQL (private subnets)
                    |                              |
                    +--> CloudWatch Logs            +--> Secrets Manager
```

## Prerequisites

- Java 17+
- Maven 3.9+
- Docker
- Terraform 1.5+
- AWS CLI configured for a deployable account

## Run locally

```bash
mvn spring-boot:run
```

Open `http://localhost:8080/health`. The default profile uses an in-memory H2 database.

The application also exposes:

- `GET /` - service welcome response
- `GET /health` - health check
- `GET /api/messages` - list stored messages
- `POST /api/messages` - create a message

Example:

```bash
curl -X POST http://localhost:8080/api/messages \
  -H 'Content-Type: application/json' \
  -d '{"text":"Hello from AWS"}'
```

## Build and test

```bash
mvn test
mvn package
docker build -t aws-3tier-java .
docker run --rm -p 8080:8080 aws-3tier-java
```

## Deploy to AWS

1. Create the infrastructure:

```bash
cd terraform
terraform init
terraform apply
```

2. Build and push the application image:

```bash
AWS_REGION=$(terraform -chdir=terraform output -raw aws_region)
ECR_URL=$(terraform -chdir=terraform output -raw ecr_repository_url)
AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)

aws ecr get-login-password --region "$AWS_REGION" |
  docker login --username AWS --password-stdin "$AWS_ACCOUNT_ID.dkr.ecr.$AWS_REGION.amazonaws.com"

docker build -t aws-3tier-java .
docker tag aws-3tier-java:latest "$ECR_URL:latest"
docker push "$ECR_URL:latest"
```

3. Deploy the image to ECS:

```bash
terraform apply -var="app_image_tag=latest" -var="app_count=1"
```

4. Open the Application Load Balancer URL:

```bash
terraform -chdir=terraform output -raw application_url
```

## Configuration

| Variable | Local default | AWS source |
|---|---|---|
| `SERVER_PORT` | `8080` | ECS task environment |
| `DB_HOST` | `localhost` | RDS endpoint |
| `DB_PORT` | `5432` | RDS port |
| `DB_NAME` | `appdb` | Terraform |
| `DB_USERNAME` | `appuser` | Terraform |
| `DB_PASSWORD` | `apppassword` | AWS Secrets Manager |

## Destroy

```bash
cd terraform
terraform destroy
```
