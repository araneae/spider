
class IndustryEditCtrl

    constructor: (@$log, @$state, @$scope, @$stateParams, @ErrorService, @Industry, @IndustryService) ->
        @$log.debug "constructing IndustryEditCtrl"
        @industryId = parseInt(@$stateParams.industryId)
        @industry = {}
        # load data from server
        @loadIndustry()

    loadIndustry: () ->
        @$log.debug "IndustryEditCtrl.loadIndustry()"
        @Industry.get({industryId: @industryId}).$promise
          .then(
            (data) =>
              @$log.debug "Promise returned #{data} Industry"
              @industry =  data
            ,
            (error) =>
                @ErrorService.error("Unable to fetch data from server")
                @$log.error "Unable to fetch data from server : #{error}"
           )
    
    save: () ->
        @$log.debug "IndustryEditCtrl.save()"
        industry = new @Industry(@industry);
        industry.$update().then( 
            (data) =>
                @ErrorService.success("Successfully update industry #{@industry.name}")
                @$log.debug "Promise returned #{data} Industry"
                @$state.go("industry")
            ,
            (error) =>
                @ErrorService.error("Unable to update #{@industry.name}")
                @$log.error "Unable to save Industry: #{error}"
            )

    cancel: () ->
        @$log.debug "IndustryEditCtrl.cancel()"
        @$state.go("industry")
    
controllersModule.controller('IndustryEditCtrl', IndustryEditCtrl)