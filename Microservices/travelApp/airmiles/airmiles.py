import random
from nameko.rpc import rpc
from nameko_redis import Redis


class AirmilesService:
    name = "airmiles_service"

    redis = Redis('airmiles_db')

    @rpc
    def get(self, name):
        miles = self.redis.get(name)
        return miles

    @rpc
    def create(self, name):
        miles = random.randint(50, 1000)
        self.redis.set(name, str(miles))
        return str(miles)
