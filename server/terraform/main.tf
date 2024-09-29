terraform {
  required_version = ">= 1.0.0"

  # Configure the backend
  backend "s3" {
    key            = "journail/terraform.state" # Path to the state file in the bucket
    region         = "us-east-1"                # Replace with your desired AWS region
    dynamodb_table = "terraform-lock"           # Optional: Lock table for state locking
    encrypt        = true                       # Optional: Encrypt the state file
  }
}

# Configure the AWS provider
provider "aws" {
  region = "us-east-1" # Replace with your desired AWS region
}

data "aws_secretsmanager_secret" "secret" {
  name = var.app_name # The name of your secret
}

data "aws_secretsmanager_secret_version" "secret_version" {
  secret_id = data.aws_secretsmanager_secret.secret.id
}

locals {
  secret_json = jsondecode(data.aws_secretsmanager_secret_version.secret_version.secret_string)
}