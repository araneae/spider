
class SubscriptionService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing SubscriptionService"

servicesModule.service('SubscriptionService', SubscriptionService)

# define the factories
#
servicesModule.factory('Subscription', ['$resource', ($resource) -> 
              $resource('/subscription/:subscriptionId', {subscriptionId: '@subscriptionId'}, {'update': {method: 'PUT'}})])