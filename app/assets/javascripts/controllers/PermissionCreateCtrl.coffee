
class PermissionCreateCtrl

    constructor: (@$log, @$scope, @$state, @ErrorService, @Permission, @PermissionService) ->
        @$log.debug "constructing PermissionCreateCtrl"
        @permission = {}
        
    save: () ->
        @$log.debug "PermissionCreateCtrl.create()"
        @Permission.save(@permission).$promise
        .then( 
            (data) =>
              @ErrorService.success("Successfully created permission #{@permission.name}")
              @$log.debug "Promise returned #{data} Permission"
              @$state.go("permissions")
            ,
            (error) =>
                @ErrorService.error("Unable to create #{@permission.name}")
                @$log.error "Unable to create Permission: #{error}"
            )

    cancel: () ->
        @$log.debug "PermissionCreateCtrl.cancel()"
        @$state.go("permissions")

controllersModule.controller('PermissionCreateCtrl', PermissionCreateCtrl)