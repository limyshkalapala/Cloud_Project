import json
import hashlib
import requests

def lambda_handler(event, context):
    action = event['input']['action']  # Access the 'action' field from the nested 'input'
    value = event['input']['value']    # Access the 'value' field from the nested 'input'

    if action == 'sha256':
        hashed_value = hashlib.sha256(value.encode('utf-8')).hexdigest()

        response = {
            'banner': "B00934899",
            'result': hashed_value,
            'arn': "arn:aws:lambda:us-east-1:209474665153:function:InvokeSHA256Lambda",
            'action': 'sha256',
            'value': value
        }

        # Send POST request to the specified URL
        url = "https://v7qaxwoyrb.execute-api.us-east-1.amazonaws.com/default/end"
        headers = {'Content-Type': 'application/json'}
        payload = json.dumps(response)

        try:
            response = requests.post(url, data=payload, headers=headers)
            response.raise_for_status()  # Raise an exception for non-2xx status codes
            return response.json()  # Return the response from the POST request
        except requests.exceptions.RequestException as e:
            return {
                'error': str(e)  # Return the error message as part of the response
            }
    else:
        return {
            'state': 'InvalidActionState',
            'input': {
                'action': action,
                'value': value
            }
        }