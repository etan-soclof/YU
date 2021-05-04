# Simple Queue

This microservice architecture contains three services: 

1) gateway - this is the api gateway to the task queue from which one can add a task to the queue or query the numebr of tasks remaining in the queue

	This is a flask application with two paths: 

		- '/' - this adds a task to the queue

		- '/get' - this returns the number of tasks remaining in the queue

2) rabbitmq service

3) processFromQ - this service accepts tasks from the queue, sleeps for 3 seconds, and repeats

To deploy, run:
'''
soclof@cloudshell:~$ kubectl apply -f queue.yaml
service/rabbit created
deployment.apps/rabbit created
service/gateway created
deployment.apps/gateway created
deployment.apps/process created
'''

on the desired cloud service.

The yaml file creates 3 deployments: gateway, process, and rabbit. It also creates 2 services - one to provide internal access to the rabbitmq container and one to provide an external endpoint to access the gateway service.

The yaml file pulls the docker images from my docker hub. The docker containers can also be built from the dockerfiles in the repositories.

I personally deployed the services on GKE. 

To find the ip address through which to access the application:

'''
soclof@cloudshell:~$ kubectl get service gateway
NAME      TYPE           CLUSTER-IP   EXTERNAL-IP    PORT(S)          AGE
gateway   LoadBalancer   10.8.0.118   23.236.50.63   5000:30865/TCP   77s
'''

In the above example, the application is running at 23.236.50.63:5000.

To add a task to the queue: 23.236.50.63:5000/

To check the number of tasks remaining in the queue: 23.236.50.63:5000/get




