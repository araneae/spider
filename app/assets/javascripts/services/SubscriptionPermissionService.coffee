
class SubscriptionPermissionService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing SubscriptionPermissionService"

        
servicesModule.service('SubscriptionPermissionService', SubscriptionPermissionService)

# define the factories
#
servicesModule.factory('SubscriptionPermission', ['$resource', ($resource) -> 
              $resource('/subscriptionPermission/:subscriptionId/permission/:permissionId', 
                {subscriptionId: '@subscriptionId', permissionId: '@permissionId'}, {'update': {method: 'PUT'}})])