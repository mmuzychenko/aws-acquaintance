#output "rds_endpoint" {
#  value = aws_db_instance.postgres.endpoint
#}
#
#output "docdb_endpoint" {
#  value = aws_docdb_cluster.docdb.endpoint
#}
#
#output "s3_bucket_name" {
#  value = aws_s3_bucket.image_bucket.bucket
#}
#
#output "sns_topic_arn" {
#  value = aws_sns_topic.image_events.arn
#}
#
#output "step_function_arn" {
#  value = aws_sfn_state_machine.delete_file_workflow.arn
#}
#
#output "user_service_url" {
#  value = "http://${aws_instance.user_service.public_dns}:8080"
#}
#
#output "image_service_url" {
#  value = "http://${aws_instance.image_service.public_dns}:8081"
#}