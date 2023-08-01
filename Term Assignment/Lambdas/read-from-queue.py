import json
import boto3

sqs_queue_name = "IncidentReportCf"

def lambda_handler(event, context):
    print("Starting the Lambda function...")

    sqs = boto3.client("sqs")
    queue_url = get_sqs_queue_url(sqs, sqs_queue_name)
    print("SQS Queue URL:", queue_url)

    messages = poll_messages_from_sqs(sqs, queue_url)
    print("Received", len(messages), "messages from the SQS queue")

    for message in messages:
        incident_report_message = json.loads(message["Body"])
        incident_type = incident_report_message.get("type")

        print("Processing message with Incident Type:", incident_type)

        if incident_type == "Man Overboard":
            delete_message_from_sqs(message["ReceiptHandle"], sqs, queue_url)
            return incident_report_message
            print("Deleted the message with Incident Type: Man Overboard")

        elif incident_type == "Oil Spill":
            delete_message_from_sqs(message["ReceiptHandle"], sqs, queue_url)
            return incident_report_message
            print("Deleted the message with Incident Type: Oil Spill")

        elif incident_type == "Hurricane":
            print("Found an incident with Incident Type: Hurricane")
            delete_message_from_sqs(message["ReceiptHandle"], sqs, queue_url)
            return incident_report_message


        delete_message_from_sqs(message["ReceiptHandle"], sqs, queue_url)
        print("Deleted the message with Incident Type:", incident_type)
    print("All messages processed.")
    return {"message": "No Hurricane incidents found."}

def get_sqs_queue_url(sqs, queue_name):
    response = sqs.get_queue_url(QueueName=queue_name)
    return response["QueueUrl"]

def poll_messages_from_sqs(sqs, queue_url):
    receive_message_request = {
        "QueueUrl": queue_url,
        "MaxNumberOfMessages": 10,
        "WaitTimeSeconds": 5
    }

    response = sqs.receive_message(**receive_message_request)
    messages = response.get("Messages", [])
    while "Messages" in response:
        messages.extend(response["Messages"])
        response = sqs.receive_message(**receive_message_request)

    return messages

def delete_message_from_sqs(receipt_handle, sqs, queue_url):
    delete_message_request = {
        "QueueUrl": queue_url,
        "ReceiptHandle": receipt_handle
    }
    
    sqs.delete_message(**delete_message_request)
