
class PermissionEditCtrl

    constructor: (@$log, @$state, @$scope, @$stateParams, @ErrorService, @Permission, @PermissionService) ->
        @$log.debug "constructing PermissionEditCtrl"
        @permissionId = parseInt(@$stateParams.permissionId)
        @permission = {}
        # load data from server
        @loadPermission()

    loadPermission: () ->
        @$log.debug "PermissionEditCtrl.loadPermission()"
        @Permission.get({permissionId: @permissionId}).$promise
          .then(
            (data) =>
              @$log.debug "Promise returned #{data} Permission"
              @permission =  data
            ,
            (error) =>
                @ErrorService.error("Unable to fetch data from server")
                @$log.error "Unable to fetch data from server : #{error}"
           )
    
    save: () ->
        @$log.debug "PermissionEditCtrl.save()"
        permission = new @Permission(@permission);
        permission.$update().then( 
            (data) =>
                @ErrorService.success("Successfully update permission #{@permission.name}")
                @$log.debug "Promise returned #{data} Permission"
                @$state.go("permissions")
            ,
            (error) =>
                @ErrorService.error("Unable to update #{@permission.name}")
                @$log.error "Unable to save Permission: #{error}"
            )

    cancel: () ->
        @$log.debug "PermissionEditCtrl.cancel()"
        @$state.go("permissions")
    
controllersModule.controller('PermissionEditCtrl', PermissionEditCtrl)