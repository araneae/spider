
class PermissionService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing PermissionService"


servicesModule.service('PermissionService', PermissionService)

# define the factories
#
servicesModule.factory('Permission', ['$resource', ($resource) -> 
              $resource('/permission/:permissionId', {permissionId: '@permissionId'}, {'update': {method: 'PUT'}})])