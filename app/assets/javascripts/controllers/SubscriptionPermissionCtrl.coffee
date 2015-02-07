
class SubscriptionPermissionCtrl

    constructor: (@$log, @$scope, @$state, @SubscriptionPermission, @ErrorService, @SubscriptionPermissionService) ->
        @$log.debug "constructing SubscriptionPermissionCtrl"
        @subscriptionPermissions = []
        @$scope.$on('contextMenu', (event, data) =>
                                    @$log.debug "received message contextMenu(#{data.menuItem})"
                                    @refresh() if data.menuItem is "refresh"
                                    @goToCreate() if data.menuItem is "create"
        )
        # fetch data from server
        @listSubscriptionPermissions()
        
    listSubscriptionPermissions: () ->
        @$log.debug "SubscriptionPermissionCtrl.listSubscriptionPermissions()"
        @SubscriptionPermissionService.loadSubscriptionPermissions()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} SubscriptionPermissions"
                @subscriptionPermissions = data
            ,
            (error) =>
                @$log.error "Unable to get SubscriptionPermissions: #{error}"
            )
    
    refresh: () ->
        @listSubscriptionPermissions()
    
    goToCreate: () ->
      @$log.debug "SubscriptionPermissionCtrl.goToCreate()"
      @$state.go("subscriptionPermissionCreate")
    
    delete: (SubscriptionPermission) ->
        @$log.debug "SubscriptionPermissionCtrl.delete(#{subscriptionPermission})"
        @SubscriptionPermission.remove({subscriptionId: subscriptionPermission.subscriptionId, permissionId : subscriptionPermission.permissionId})  
        @listSubscriptionPermissions()
    
    goToEdit: (subscriptionPermission) ->
        @$log.debug "SubscriptionPermissionCtrl.goToEdit(#{subscriptionPermission})"
        @$state.go("subscriptionPermissionEdit", {subscriptionId: subscriptionPermission.subscriptionId, permissionId : subscriptionPermission.permissionId})

controllersModule.controller('SubscriptionPermissionCtrl', SubscriptionPermissionCtrl)