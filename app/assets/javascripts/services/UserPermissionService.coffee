
class UserPermissionService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing UserPermissionService"
        @permissions = ['site.admin']

    hasPermission: (permission) ->
        @$log.debug "UserPermissionService.hasPermission(#{permission})"
        permission = permission.trim()
        for item in @permissions
          if (angular.isString(item) and item is permission)
            return (true)
        false

servicesModule.service('UserPermissionService', UserPermissionService)

# define the factories
#
