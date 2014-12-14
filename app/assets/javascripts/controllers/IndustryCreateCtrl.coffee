
class IndustryCreateCtrl

    constructor: (@$log, @$scope, @$state, @ErrorService, @Industry, @IndustryService) ->
        @$log.debug "constructing IndustryCreateCtrl"
        @industry = {}
        
    save: () ->
        @$log.debug "IndustryCreateCtrl.create()"
        @Industry.save(@industry).$promise
        .then( 
            (data) =>
              @ErrorService.success("Successfully created industry #{@industry.name}")
              @$log.debug "Promise returned #{data} Industry"
              @$state.go("industries")
            ,
            (error) =>
                @ErrorService.error("Unable to create #{@industry.name}")
                @$log.error "Unable to create Industry: #{error}"
            )

    cancel: () ->
        @$log.debug "IndustryCreateCtrl.cancel()"
        @$state.go("industries")

controllersModule.controller('IndustryCreateCtrl', IndustryCreateCtrl)