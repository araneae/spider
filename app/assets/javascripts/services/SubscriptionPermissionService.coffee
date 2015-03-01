
class SubscriptionPermissionService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing SubscriptionPermissionService"

    loadSubscriptionPermissions: () ->
        @$log.debug "SubscriptionPermissionService.loadSubscriptionPermissions()"
        deferred = @$q.defer()

        @$http.get("/subscriptionPermission")
        .success((data, status, headers) =>
                @$log.info("Successfully loaded subscription permission - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to load subscription permission - status #{status}")
                deferred.reject(data)
            )
        deferred.promise
        
servicesModule.service('SubscriptionPermissionService', SubscriptionPermissionService)

# define the factories
#
servicesModule.factory('SubscriptionPermission', ['$resource', ($resource) -> 
              $resource('/subscriptionPermission/:subscriptionId/permission/:permissionId', {subscriptionId: '@subscriptionId', permissionId: '@permissionId'}, {'update': {method: 'PUT'}})])