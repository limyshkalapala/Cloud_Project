import json
import hashlib
import requests

def lambda_handler(event, context):
    action = event['input']['action']  # Access the 'action' field from the nested 'input'
    value = event['input']['value']    # Access the 'value' field from the nested 'input'

    if action == 'md5':
        hashed_value = hashlib.md5(value.encode('utf-8')).hexdigest()

        response = {
            'banner': "B00934899",
            'result': hashed_value,
            'arn': "arn:aws:lambda:us-east-1:209474665153:function:InvokeMD5Lambda",
            'action': 'md5',
            'value': value
        }

        # Construct the data to be sent in the POST request
        post_data = {
            "banner": response["banner"],
            "result": response["result"],
            "arn": response["arn"],
            "action": response["action"],
            "value": response["value"]
        }

        # Send the POST request to the endpoint
        try:
            response = requests.post("https://v7qaxwoyrb.execute-api.us-east-1.amazonaws.com/default/end", json=post_data)
            response.raise_for_status()
        except requests.exceptions.RequestException as e:
            # If there is an error in sending the request, handle it here
            return {
                'state': 'PostRequestError',
                'error_message': str(e)
            }

        return response.json()
    else:
        return {
            'state': 'InvalidActionState',
            'input': {
                'action': action,
                'value': value
            }
        }