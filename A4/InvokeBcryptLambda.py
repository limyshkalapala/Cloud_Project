import json
import bcrypt
import requests

def lambda_handler(event, context):
    action = event['input']['action']  # Access the 'action' field from the nested 'input'
    value = event['input']['value']    # Access the 'value' field from the nested 'input'

    if action == 'bcrypt':
        # Hash the input value using bcrypt
        hashed_value = bcrypt.hashpw(value.encode('utf-8'), bcrypt.gensalt()).decode('utf-8')

        response = {
            'banner': "B00934899",
            'result': hashed_value,
            'arn': "arn:aws:lambda:us-east-1:209474665153:function:InvokeBcryptLambda",
            'action': 'bcrypt',
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

            # Attempt to parse the JSON response
            result_data = response.json()

            # Return None if the JSON response is empty
            if not result_data:
                return None

            return result_data
        except requests.exceptions.RequestException as e:
            # If there is an error in sending the request, handle it here
            return {
                'state': 'PostRequestError',
                'error_message': str(e)
            }
        except json.JSONDecodeError as e:
            # If the response is not in valid JSON format, handle the error
            return {
                'state': 'InvalidResponseFormat',
                'error_message': str(e)
            }
    else:
        return {
            'state': 'InvalidActionState',
            'input': {
                'action': action,
                'value': value
            }
        }
