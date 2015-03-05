
class SubscriptionPermissionCtrl

    constructor: (@$log, @$scope, @$state, @SubscriptionPermission, @ErrorService, 
                  @SubscriptionPermissionService,@Permission,@Subscription,@UtilityService) ->
        @$log.debug "constructing SubscriptionPermissionCtrl"
        @subscriptionPermissions = []
        @$scope.$on('contextMenu', (event, data) =>
                                    @$log.debug "received message contextMenu(#{data.menuItem})"
                                    @refresh() if data.menuItem is "refresh"
                                    @goToCreate() if data.menuItem is "create"
        )
        # fetch data from server
        @permissions = []
        @subscriptions = []
        @$log.debug "SubscriptionPermissionCtrl Constructor called"
        #@listSubscriptionPermissions()
        @listPermissions()
            
    listSubscriptionPermissions: () ->
        @$log.debug "SubscriptionPermissionCtrl.listSubscriptionPermissions()"
        @SubscriptionPermission.query().$promise
        .then(
            (data) =>
                @$log.debug "Promise returned SubscriptionPermissions"    
                for subPerm in data 
                  @permissionObj = @UtilityService.findByProperty(@permissions,'permissionId',subPerm.permissionId)
                  @subscriptionObj = @UtilityService.findByProperty(@subscriptions,'subscriptionId',subPerm.subscriptionId)
                  obj = new Object()
                  obj["permissionName"] = @permissionObj.name
                  obj["subscriptionName"] = @subscriptionObj.name
                  obj["permissionId"] = @permissionObj.permissionId
                  obj["subscriptionId"] = @subscriptionObj.subscriptionId
                  @subscriptionPermissions.push(obj)
            ,
            (error) =>
                @$log.error "Unable to get SubscriptionPermissions: #{error}"
            )
            
    listPermissions: () ->
        @$log.debug "SubscriptionAddPermissionCtrl.listPermissions()"
        @Permission.query().$promise
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Permissions"
                @permissions = data
                @loadSubscription()
            ,
            (error) =>
                @$log.error "Unable to get Permissions: #{error}"
            )
            
    loadSubscription: () ->
        @$log.debug "SubscriptionAddPermissionCtrl.loadSubscription()"
        @Subscription.query().$promise.then(
          (data) =>
              @$log.debug "Promise returned #{angular.toJson(data)} subscription"
              @subscriptions = data
              @listSubscriptionPermissions()
          ,
          (error) =>
              @$log.error "Unable to fetch subscription: #{error}"
              @ErrorService.error("Unable to fetch subscription from server!")
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
        @$state.go("subscriptionAddPermission", {subscriptionId: subscriptionPermission.subscriptionId})

controllersModule.controller('SubscriptionPermissionCtrl', SubscriptionPermissionCtrl)