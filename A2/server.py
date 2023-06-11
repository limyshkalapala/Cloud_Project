import grpc
from concurrent import futures
import computeandstorage_pb2
import computeandstorage_pb2_grpc
import boto3
import logging
from grpc_reflection.v1alpha import reflection
import re

logging.basicConfig(level=logging.INFO)

# Configure the AWS credentials and region
session = boto3.Session(
    aws_access_key_id='AKIA53OXLYEII7N4IUXR',
    aws_secret_access_key='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',
    region_name='us-east-1')

s3_client = session.client('s3')

class EC2OperationsServicer(computeandstorage_pb2_grpc.EC2OperationsServicer):
    def StoreData(self, request, context):
        data = request.data
        bucket_name = 'b00934899-profile-limysh-kalapala'
        object_key = 'object.txt'
        try:
            s3_client.put_object(Body=data.encode('utf-8'), Bucket=bucket_name, Key=object_key)            
            s3_url = f"https://{bucket_name}.s3.us-east-1.amazonaws.com/{object_key}"
            reply = computeandstorage_pb2.StoreReply(s3uri=s3_url)
            return reply
        except Exception as e:
            logging.error("Error: Failed to store data.")
            logging.error("Error message:", str(e))
            context.set_code(grpc.StatusCode.INTERNAL)
            context.set_details("Failed to store data.")
            return computeandstorage_pb2.StoreReply()
    def AppendData(self, request, context):
        data = request.data
        bucket_name = 'b00934899-profile-limysh-kalapala'
        object_key = 'object.txt'

        try:
            response = s3_client.get_object(Bucket=bucket_name, Key=object_key)
            existing_content = response['Body'].read()
            updated_content = existing_content + data.encode('utf-8')
            s3_client.put_object(Body=updated_content, Bucket=bucket_name, Key=object_key)
            return computeandstorage_pb2.AppendReply()
        except Exception as e:
            logging.error("Error: Failed to append data to file.")
            logging.error("Error message:", str(e))
            context.set_code(grpc.StatusCode.INTERNAL)
            context.set_details("Failed to append data to file.")
            return computeandstorage_pb2.AppendReply()

    def DeleteFile(self, request, context):
        s3_url ='https://b00934899-profile-limysh-kalapala.s3.us-east-1.amazonaws.com/object.txt'
        try:
            match = re.match(r'^https://([\w\-\.]+)\.s3\.[\w\-]+\.amazonaws\.com/(.+)$', s3_url)
            if match:
                bucket_name = match.group(1)
                object_key = match.group(2)
                s3_client.delete_object(Bucket=bucket_name, Key=object_key)
                return computeandstorage_pb2.DeleteReply()
            else:
                logging.error("Error: Invalid S3 URL format.")
                context.set_code(grpc.StatusCode.INVALID_ARGUMENT)
                context.set_details("Invalid S3 URL format.")
                return computeandstorage_pb2.DeleteReply()
        except Exception as e:
            logging.error("Error: Failed to delete file.")
            logging.error("Error message:", str(e))
            context.set_code(grpc.StatusCode.INTERNAL)
            context.set_details("Failed to delete file.")
            return computeandstorage_pb2.DeleteReply()

def serve():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    computeandstorage_pb2_grpc.add_EC2OperationsServicer_to_server(EC2OperationsServicer(), server)
    SERVICE_NAMES = (
        computeandstorage_pb2.DESCRIPTOR.services_by_name['EC2Operations'].full_name,
        reflection.SERVICE_NAME,
    )
    reflection.enable_server_reflection(SERVICE_NAMES, server)
    server.add_insecure_port('[::]:9000')
    server.start()
    logging.info("Server started on port 9000")
    try:
        while True:
            pass
    except KeyboardInterrupt:
        server.stop(0)

if __name__ == '__main__':
    serve()
