
class SubscriptionService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing SubscriptionService"

    deletePermission: (subscriptionId) ->
        @$log.debug "SubscriptionService.delete #{subscriptionId}"
        @$http.delete("/subscription/#{subscriptionId}")


servicesModule.service('SubscriptionService', SubscriptionService)

# define the factories
#
servicesModule.factory('Subscription', ['$resource', ($resource) -> 
              $resource('/subscription/:subscriptionId', {subscriptionId: '@subscriptionId'}, {'update': {method: 'PUT'}})])