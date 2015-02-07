
class SubscriptionEditCtrl

    constructor: (@$log, @$state, @$scope, @$stateParams, @ErrorService, @Subscription, @SubscriptionService) ->
        @$log.debug "constructing SubscriptionEditCtrl"
        @subscriptionId = parseInt(@$stateParams.subscriptionId)
        @subscription = {}
        # load data from server
        @loadSubscription()

    loadSubscription: () ->
        @$log.debug "SubscriptionEditCtrl.loadSubscription()"
        @Subscription.get({subscriptionId: @subscriptionId}).$promise
          .then(
            (data) =>
              @$log.debug "Promise returned #{data} Subscription"
              @subscription =  data
            ,
            (error) =>
                @ErrorService.error("Unable to fetch data from server")
                @$log.error "Unable to fetch data from server : #{error}"
           )
    
    save: () ->
        @$log.debug "SubscriptionEditCtrl.save()"
        subscription = new @Subscription(@subscription);
        subscription.$update().then( 
            (data) =>
                @ErrorService.success("Successfully update subscription #{@subscription.name}")
                @$log.debug "Promise returned #{data} Subscription"
                @$state.go("subscriptions")
            ,
            (error) =>
                @ErrorService.error("Unable to update #{@subscription.name}")
                @$log.error "Unable to save Subscription: #{error}"
            )

    cancel: () ->
        @$log.debug "SubscriptionEditCtrl.cancel()"
        @$state.go("subscriptions")
    
controllersModule.controller('SubscriptionEditCtrl', SubscriptionEditCtrl)