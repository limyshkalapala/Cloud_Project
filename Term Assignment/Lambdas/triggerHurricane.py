import json
import boto3
import base64
import time

def lambda_handler(event, context):

    image_data_base64 = event.get('image_data_base64', '')
    if image_data_base64:
        image_blob = base64.b64decode(image_data_base64.split(",")[1])

        s3_key = "incident_images/hurricane_images/hurricane_image_" + str(int(time.time())) + ".jpg"

        s3_client = boto3.client('s3')
        s3_client.put_object(Bucket='b00934899-incident-reports-cf-final', Key=s3_key, Body=image_blob, ContentType='image/jpeg')

        s3_url = f"https://b00934899-incident-reports-cf-final.s3.amazonaws.com/{s3_key}"

        event["image_data_base64"] = s3_url

    name = event.get('name', '')
    place = event.get('place', '')
    incident_report = event.get('incident_report', '')

    subject = "Hurricane Incident Report"
    message = f"An incident of Hurricane has been reported for {place}. Please find the details below.\n"
    message += f"Name: {name}\n"
    message += f"Place: {place}\n"
    message += f"Incident Report: {incident_report}\n\n"
    message += "The image of the incident is attached to this email.\n\n"
    message += f"Image URL: {s3_url}\n\n"
    sns_topic_arn = "arn:aws:sns:us-east-1:196551330648:HurricaneAlertCfTopic"
    sns = boto3.client('sns')
    sns.publish(TopicArn=sns_topic_arn, Subject=subject, Message=message)
    return {
        "statusCode": 200,
        "body": json.dumps({"message": "Data sent to SNS topic successfully."})
    }
