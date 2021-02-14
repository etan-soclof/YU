import json
from nameko.rpc import RpcProxy
from nameko.web.handlers import http

class GatewayService:
    name = 'gateway'

    bookings_rpc = RpcProxy('bookings_service')
    airmiles_rpc = RpcProxy('airmiles_service')

    @http('GET', '/')
    def test (self, request):
        return "success!"

    @http('GET', '/booking/<string:name>')
    def get_airport(self, request, name):
        trip = self.bookings_rpc.get(name)
        return json.dumps({'trip': trip})

    @http('POST', '/booking')
    def post_airport(self, request):
        data = json.loads(request.get_data(as_text=True))
        success = self.bookings_rpc.create(data["name"], data['from_airport'], data['to_airport'])
        return success

    @http('GET', '/miles/<string:name>')
    def get_trip(self, request, name):
        miles = self.airmiles_rpc.get(name)
        return miles