def lambda_handler(event, context):
    action = event['action']
    value = event['value']

    if action == 'sha256':
        return {
            'state': 'InvokeSHA256Lambda',
            'input': {
                'action': action,
                'value': value
            }
        }
    elif action == 'md5':
        return {
            'state': 'InvokeMD5Lambda',
            'input': {
                'action': action,
                'value': value
            }
        }
    elif action == 'bcrypt':
        return {
            'state': 'InvokeBcryptLambda',
            'input': {
                'action': action,
                'value': value
            }
        }
    else:
        return {
            'state': 'InvalidActionState',
            'input': {
                'action': action,
                'value': value
            }
        }
