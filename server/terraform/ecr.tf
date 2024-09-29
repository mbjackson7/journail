resource "aws_ecr_repository" "ecr_repo" {
  name = var.app_name
}

resource "null_resource" "docker_build_and_push" {
  triggers = {
    "maintenance" = sha1(join("", [for f in fileset("../", "*") : filesha1("../${f}")]))
  }

  provisioner "local-exec" {
    command = <<EOT
            mvn -f ../pom.xml clean package
            docker build -t ${var.app_name} ../
            aws ecr get-login-password --region ${var.primary_region} | docker login --username AWS --password-stdin ${var.account_id}.dkr.ecr.${var.primary_region}.amazonaws.com
            docker tag ${var.app_name}:latest ${var.account_id}.dkr.ecr.${var.primary_region}.amazonaws.com/${aws_ecr_repository.ecr_repo.name}:latest
            docker push ${var.account_id}.dkr.ecr.${var.primary_region}.amazonaws.com/${aws_ecr_repository.ecr_repo.name}:latest
        EOT
  }

  depends_on = [aws_ecr_repository.ecr_repo]
}

data "aws_ecr_image" "service_image" {
  repository_name = aws_ecr_repository.ecr_repo.name
  image_tag       = "latest"

  depends_on = [null_resource.docker_build_and_push]
}