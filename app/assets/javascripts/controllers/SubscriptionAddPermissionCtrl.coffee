
class SubscriptionAddPermissionCtrl

    constructor: (@$log, @$scope, @$state, @Permission, @ErrorService, @SubscriptionPermissionService, 
                  @$stateParams, @UtilityService, @Subscription, @SubscriptionPermission) ->
        @$log.debug "constructing SubscriptionAddPermissionCtrl"
        @subscriptionPermissions = {}
        @permissions = []
        @subscription = {}
        @selectedPermissions = []
        @subscriptionId = @UtilityService.parseInteger(@$stateParams.subscriptionId)       
        # fetch data from server
        @listPermissions()
        @loadSubscription(@subscriptionId)
        
    loadSubscription: (subscriptionId) ->
        @$log.debug "SubscriptionAddPermissionCtrl.loadSubscription()"
        @Subscription.get({subscriptionId: subscriptionId}).$promise.then(
          (data) =>
              @$log.debug "Promise returned #{angular.toJson(data)} subscription"
              @subscription = data
              @title = "Subscription in #{data.name}"
          ,
          (error) =>
              @$log.error "Unable to fetch subscription: #{error}"
              @ErrorService.error("Unable to fetch subscription from server!")
          )
    
    listSubscriptionPermissions: (subscriptionId) ->
        @$log.debug "SubscriptionAddPermissionCtrl.listSubscriptionPermissions()"
        @SubscriptionPermission.query({subscriptionId: subscriptionId}).$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} SubscriptionPermissions"    
                for obj in data
                  permissinObj = @UtilityService.findByProperty(@permissions,'permissionId',obj.permissionId)
                  @selectedPermissions.push(permissinObj)
                  @subscriptionPermissions[obj.permissionId] = angular.copy(obj) 
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
                @listSubscriptionPermissions(@subscriptionId)
            ,
            (error) =>
                @$log.error "Unable to get Permissions: #{error}"
            )
        
    cancel: () ->
        @$log.debug "SubscriptionAddPermissionCtrl.cancel()"
        @$state.go("subscriptions")
        
    save: () ->
      @$log.debug "SubscriptionAddPermissionCtrl.save()"
      deletedObjs = []
      newObjs = []
      for obj in @selectedPermissions
        if(!@subscriptionPermissions[obj.permissionId])
            newObjs.push(obj)
      for key,obj of @subscriptionPermissions
        if(!@UtilityService.findByProperty(@selectedPermissions,'permissionId', obj.permissionId))  
          deletedObjs.push(obj.permissionId)     
      for permissionId in deletedObjs
         @SubscriptionPermission.remove({subscriptionId: @subscriptionId, permissionId: permissionId}).$promise.then(
            (data) =>
                @$log.debug "Removed SubscriptionPermissions"
            ,
            (error) =>
                @$log.error "Unable to remove SubscriptionPermissions: #{error}"
            )
      for obj in newObjs
         @SubscriptionPermission.save({subscriptionId: @subscriptionId, permissionId: obj.permissionId}).$promise.then( 
           (data) =>
                @$log.debug "Saved SubscriptionPermissions"
           ,
           (error) =>
                @$log.error "Unable to save SubscriptionPermissions: #{error}"
           )                     
controllersModule.controller('SubscriptionAddPermissionCtrl', SubscriptionAddPermissionCtrl)