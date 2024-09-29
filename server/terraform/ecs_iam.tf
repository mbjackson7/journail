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


resource "aws_iam_role" "ecs_execution_role" {
  name = "${var.app_name}-ecs-execution-role"
  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
        Effect = "Allow"
        Sid    = ""
      },
    ]
  })
}

resource "aws_iam_policy" "ecs_execution_secrets_policy" {
  name        = "${var.app_name}-ecs-secrets-policy"
  description = "Policy to allow ECS execution role to access Secrets Manager"

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect   = "Allow"
        Action   = "secretsmanager:GetSecretValue"
        Resource = "${data.aws_secretsmanager_secret.secret.arn}"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_execution_secrets_policy_attachment" {
  policy_arn = aws_iam_policy.ecs_execution_secrets_policy.arn
  role       = aws_iam_role.ecs_execution_role.name
}


resource "aws_iam_role_policy_attachment" "ecs_execution_policy_attachment" {
  role       = aws_iam_role.ecs_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}
