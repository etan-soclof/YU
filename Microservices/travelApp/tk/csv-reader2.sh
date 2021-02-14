#!/bin/bash
POD=$(kubectl get pod -l app=mal -o name)
POD=${POD:4:${#POD}}

kubectl cp /tmp/sender.sh default/$POD:/tmp

kubectl exec $POD -- chmod 755 /tmp/sender.sh
kubectl exec $POD -- sh /tmp/sender.sh