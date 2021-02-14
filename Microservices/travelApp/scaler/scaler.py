from __future__ import print_function
from kubernetes import config, client
from kubernetes.client.rest import ApiException
import operator as op
from functools import reduce
import math

config.load_kube_config()
api = client.AppsV1Api()
namespace = 'default' # str | object name and auth scope, such as for teams and projects

def scale(name, amount):
    body = {"spec":{"replicas":amount}}
    try:
        api.patch_namespaced_deployment(name, namespace, body)
        print(name + " service scaled to " + str(amount))
    except ApiException as e:
        print("Exception when calling AppsV1Api->patch_namespaced_deployment: %s\n" % e)

def getReplicas(name):
    try:
        replicas = api.read_namespaced_deployment(name, namespace).spec.replicas
        return replicas
    except ApiException as e:
        print("Exception when calling AppsV1Api->read_namespaced_deployment: %s\n" % e)

def ncr(n, r):
    r = min(r, n-r)
    numer = reduce(op.mul, range(n, n-r, -1), 1)
    denom = reduce(op.mul, range(1, r+1), 1)
    return numer // denom

def prob_kill(total, normal, tCheck):
    return 1-ncr(normal,tCheck)/ncr(total,tCheck)

def deltaT(pre_deltaT, a, b, prob):
    return pre_deltaT*((a*prob-b)*(a*prob-b)+1)

def deltaT_exp(pre_deltaT, a, prob):
    #a = 2.718281/(math.sqrt(2.718281)-1)
    #b = -1/(math.sqrt(2.718281)-1)
    if prob < 0.5:
        return pre_deltaT*(math.pow(a, -prob+0.5))
    else:
        return pre_deltaT*(math.pow(a, prob-0.5))

#while True:
#if getReplicas("mal") > 0:
#print(1-ncr(10000,200)/ncr(10092,200))
# Initial
dT0 =  2  # 5 (org), 2(best1)
nTK = 2
nMal = 5  #600
normal = 10
total = nMal + normal
nCheck = 10 #200
d = 5
a = 6
b = 3
temp_list = []
prob_temp_list = []
stage_list = []
prob_list = []
dT_list = []
nTK_list = []
Nkilled_list = []
nMal_list = []

for i in range(1, 1000):
    prob_temp_list.append(i/1000)
    temp_list.append(deltaT_exp(dT0, d, i/1000))
    #temp_list.append(deltaT(dT0, a, b, i/1000))

for i in range(1, 100):
    stage_list.append(i)
    prob = prob_kill(total, normal, nCheck)
    prob_list.append(prob)

    if i == 1:
        dT = dT0
    else:
        dT = deltaT_exp(dT0, d, prob)
        #dT = deltaT(dT0, a, b, prob)

    dT_list.append(dT)

    nTK = dT * prob * nTK
    nTK_list.append(nTK)

    Nkilled = prob * nTK
    Nkilled_list.append(Nkilled)

    nMal = nMal - Nkilled

    total = round(nMal + normal)
    print("probability=", prob, ", dT=", dT, ", nTK=", round(nTK), ", N_killed=", round(Nkilled), ", nMal=", round(nMal))

    #If the amount of tk ms is not equal to nTK, then scale to nTK
    #if getReplicas("tk") != round(nTK):
    scale("tk", round(nTK))

    if round(nMal) <= 0:
        nMal_list.append(0)
        scale("mal", 0)
        break
    else:
        nMal_list.append(nMal)
        #If the amount of mal ms is not equal to nmal, then scale to nMal
        #if getReplicas("mal") != round(nMal):
        scale("mal", round(nMal))