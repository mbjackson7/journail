resource "aws_iam_role" "ecs_task_role" {
  name = "${var.app_name}-ecs-task-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
        Action = "sts:AssumeRole"
      }
    ]
  })
}

resource "aws_iam_policy" "bedrock_policy" {
  name        = "${var.app_name}-bedrock-policy"
  description = "Policy to allow access to Bedrock services"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "bedrock:InvokeModel", # Allow invoking Bedrock models
          "bedrock:GetModel",    # Allow getting model information
          "bedrock:ListModels"   # Allow listing available models
        ]
        Resource = "*" # Adjust as needed for specific resources
      }
    ]
  })
}

resource "aws_iam_policy" "secrets_manager_policy" {
  name        = "${var.app_name}-secrets-manager-policy"
  description = "Policy to allow access to Secrets Manager"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "secretsmanager:GetSecretValue", # Allow getting secret values
          "secretsmanager:DescribeSecret"  # Allow describing secrets
        ]
        Resource = "*" # Adjust as needed for specific resources
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "attach_bedrock_policy" {
  role       = aws_iam_role.ecs_task_role.name
  policy_arn = aws_iam_policy.bedrock_policy.arn
}

resource "aws_iam_role_policy_attachment" "attach_secrets_manager_policy" {
  role       = aws_iam_role.ecs_task_role.name
  policy_arn = aws_iam_policy.secrets_manager_policy.arn
}
