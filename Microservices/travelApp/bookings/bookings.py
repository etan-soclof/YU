from nameko.rpc import rpc
from nameko_redis import Redis
import json
from nameko.rpc import RpcProxy
from prometheus_client import start_http_server, Counter
c = Counter('requests_total', 'Total number of post requests')

class BookingsService:
    name = "bookings_service"

    redis = Redis("bookings_db")
    airmiles_rpc = RpcProxy('airmiles_service')
    start_http_server(8000)

    @rpc
    def get(self, name):
        airports = self.redis.get(name)
        return airports


    @rpc
    def create(self, name, from_airport, to_airport):
        global c
        c.inc()
        self.redis.set(name, json.dumps({
            "from": from_airport,
            "to": to_airport
        }))
        miles = self.airmiles_rpc.create(name)
        return "trip created and " + miles + " miles added to account"
