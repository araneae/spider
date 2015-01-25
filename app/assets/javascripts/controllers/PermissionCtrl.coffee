
class PermissionCtrl

    constructor: (@$log, @$scope, @$state, @Permission, @ErrorService, @PermissionService) ->
        @$log.debug "constructing PermissionCtrl"
        @permissions = []
        @$scope.$on('contextMenu', (event, data) =>
                                    @$log.debug "received message contextMenu(#{data.menuItem})"
                                    @refresh() if data.menuItem is "refresh"
                                    @goToCreate() if data.menuItem is "create"
        )
        # fetch data from server
        @listPermissions()
        
    listPermissions: () ->
        @$log.debug "PermissionCtrl.listPermissions()"
        @Permission.query().$promise
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Permissions"
                @permissions = data
            ,
            (error) =>
                @$log.error "Unable to get Permissions: #{error}"
            )
    
    refresh: () ->
        @listPermissions()
    
    goToCreate: () ->
      @$log.debug "PermissionCtrl.goToCreate()"
      @$state.go("permissionCreate")
    
    delete: (permission) ->
        @$log.debug "PermissionCtrl.delete(#{permission})"
        @Permission.remove({permissionId: permission.permissionId})
        @listPermissions()
    
    goToEdit: (permission) ->
        @$log.debug "PermissionCtrl.goToEdit(#{permission})"
        @$state.go("permissionEdit", {permissionId: permission.permissionId})

controllersModule.controller('PermissionCtrl', PermissionCtrl)