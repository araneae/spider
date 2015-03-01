
class SubscriptionCtrl

    constructor: (@$log, @$scope, @$state, @Subscription, @ErrorService, @SubscriptionService) ->
        @$log.debug "constructing SubscriptionCtrl"
        @subscriptions = []
        @$scope.$on('contextMenu', (event, data) =>
                                    @$log.debug "received message contextMenu(#{data.menuItem})"
                                    @refresh() if data.menuItem is "refresh"
                                    @goToCreate() if data.menuItem is "create"
        )
        # fetch data from server
        @listSubscriptions()
        
    listSubscriptions: () ->
        @$log.debug "SubscriptionCtrl.listSubscriptions()"
        @Subscription.query().$promise
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Subscriptions"
                @subscriptions = data
            ,
            (error) =>
                @$log.error "Unable to get Subscriptions: #{error}"
            )
    
    refresh: () ->
        @listSubscriptions()
    
    goToCreate: () ->
      @$log.debug "SubscriptionCtrl.goToCreate()"
      @$state.go("subscriptionCreate")
    
    delete: (subscription) ->
        @$log.debug "SubscriptionCtrl.delete(#{subscription})"
        @Subscription.remove({subscriptionId: subscription.subscriptionId})
        @listSubscriptions()
    
    goToEdit: (subscription) ->
        @$log.debug "SubscriptionCtrl.goToEdit(#{subscription})"
        @$state.go("subscriptionEdit", {subscriptionId: subscription.subscriptionId})
        
    goToAddPermission: (subscription) ->
        @$log.debug "SubscriptionCtrl.goToAddPermission(#{subscription})"
        @$state.go("subscriptionAddPermission", {subscriptionId: subscription.subscriptionId})
      

controllersModule.controller('SubscriptionCtrl', SubscriptionCtrl)