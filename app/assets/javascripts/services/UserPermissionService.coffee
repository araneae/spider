
class UserPermissionService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing UserPermissionService"
        @permissions
    
    initUserPermissions: () ->
        @$log.debug "UserPermissionService.initUserPermissions()"
        if (!@permissions)
          @$http.get("/userPermission")
              .success((data, status, headers, config) =>
                    @$log.info("Successfully fetched permissions - status #{status} data #{angular.toJson(data)}")
                    @permissions = data
                  )
               .error((data, status, headers, config) =>
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
