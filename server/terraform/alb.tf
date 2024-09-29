resource "aws_lb" "lb" {
  name               = "${var.app_name}-lb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.sg.id]
  subnets            = [aws_subnet.public_subnet.id, aws_subnet.public_subnet_2.id]

  enable_deletion_protection = false # Set to true in production
  tags = {
    Name = "${var.app_name}-lb"
  }
}

resource "aws_lb_listener" "lb_listener" {
  load_balancer_arn = aws_lb.lb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.lb_tg.arn
  }
}

resource "aws_lb_target_group" "lb_tg" {
  name        = "${var.app_name}-tg"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = aws_vpc.vpc.id
  target_type = "ip"

  health_check {
    path                = "/api/journals/health"
    interval            = 30
    timeout             = 5
    healthy_threshold   = 2
    unhealthy_threshold = 2
  }
}
