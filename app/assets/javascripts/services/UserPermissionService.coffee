
class UserPermissionService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing UserPermissionService"
        @permissions
        @loadUserPermissions() if (!@permissions)

    loadUserPermissions: () ->
        @$log.debug "UserPermissionService.loadUserPermissions()"
        @$http.get("/userPermission")
              .success((data, status, headers) =>
                    @$log.info("Successfully fetched permissions - status #{status}")
                    @permissions = data
                  )
               .error((data, status, headers) =>
                    @$log.error("Failed to fetch permissions - status #{status}")
                  )

    hasPermission: (permission) ->
        @$log.debug "UserPermissionService.hasPermission(#{permission})"
        permission = permission.trim()
        if (@permissions)
          for item in @permissions
            if (angular.isString(item) and item is permission)
              return (true)
        false

servicesModule.service('UserPermissionService', UserPermissionService)

# define the factories
#
