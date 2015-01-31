
class SubscriptionCreateCtrl

    constructor: (@$log, @$scope, @$state, @ErrorService, @Subscription, @SubscriptionService) ->
        @$log.debug "constructing SubscriptionCreateCtrl"
        @subscription = {}
        
    save: () ->
        @$log.debug "SubscriptionCreateCtrl.create()"
        @Subscription.save(@subscription).$promise
        .then( 
            (data) =>
              @ErrorService.success("Successfully created permission #{@Subscription.name}")
              @$log.debug "Promise returned #{data} Subscription"
              @$state.go("subscriptions")
            ,
            (error) =>
                @ErrorService.error("Unable to create #{@Subscription.name}")
                @$log.error "Unable to create Subscription: #{error}"
            )

    cancel: () ->
        @$log.debug "SubscriptionCreateCtrl.cancel()"
        @$state.go("subscriptions")

controllersModule.controller('SubscriptionCreateCtrl', SubscriptionCreateCtrl)