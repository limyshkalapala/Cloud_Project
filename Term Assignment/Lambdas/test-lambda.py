import json
import boto3


sqs = boto3.client('sqs')
queue_url = 'https://sqs.us-east-1.amazonaws.com/196551330648/IncidentReportCf'
def lambda_handler(event, context):
    try:
        name = event.get('name')
        place = event.get('place')
        incident_report = event.get('incident_report')
        image_data_base64 = event.get('image_data_base64')
        type = event.get('type')

        incident = {
            "name": name,
            "place": place,
            "incident_report": incident_report,
            "type": type
        }

        if image_data_base64:
            incident["image_data_base64"] = image_data_base64

        message_body = json.dumps(incident)
        

        response = sqs.send_message(
            QueueUrl=queue_url,
            MessageBody=message_body
        )
        print(f"Published data to SQS: {message_body}")
        
        message_id = response['MessageId']
        print(f"Published message to SQS: {message_id}")

        response = {
            "statusCode": 200,
            "body": json.dumps({"message": "Incident report submitted successfully", "message_id": message_id})
        }
    except Exception as e:
        response = {
            "statusCode": 500,
            "body": json.dumps({"error": str(e)})
        }
    
    return json.dumps(response)
