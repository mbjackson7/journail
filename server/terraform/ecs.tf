resource "aws_ecs_cluster" "ecs_cluster" {
  name = "${var.app_name}-cluster"
}

resource "aws_ecs_task_definition" "ecs_task_definition" {
  family                   = "${var.app_name}-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256" # Adjust as needed
  memory                   = "512" # Adjust as needed
  task_role_arn            = aws_iam_role.ecs_task_role.arn
  execution_role_arn       = aws_iam_role.ecs_execution_role.arn


  container_definitions = jsonencode([{
    name      = "${var.app_name}-container"
    image     = "${aws_ecr_repository.ecr_repo.repository_url}:latest"
    cpu       = 256
    memory    = 512
    essential = true

    portMappings = [{
      containerPort = 8080
      hostPort      = 8080
      protocol      = "tcp"
    }]

    secrets = [
      for key, value in local.secret_json : {
        name      = key
        valueFrom = "${data.aws_secretsmanager_secret.secret.arn}"
      }
    ]
  }])

  depends_on = [data.aws_ecr_image.service_image]
}

resource "aws_ecs_service" "ecs_service" {
  name            = "${var.app_name}-service"
  cluster         = aws_ecs_cluster.ecs_cluster.id
  task_definition = aws_ecs_task_definition.ecs_task_definition.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets          = [aws_subnet.public_subnet.id] # Replace with your subnet IDs
    security_groups  = [aws_security_group.sg.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.lb_tg.arn
    container_name   = "${var.app_name}-container"
    container_port   = 8080
  }
}

