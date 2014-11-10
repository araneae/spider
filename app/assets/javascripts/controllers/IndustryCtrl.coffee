
class IndustryCtrl

    constructor: (@$log, @$scope, @$state, @Industry, @ErrorService, @IndustryService) ->
        @$log.debug "constructing IndustryCtrl"
        @industries = []
        @$scope.$on('contextMenu', (event, data) =>
                                    @$log.debug "received message contextMenu(#{data.menuItem})"
                                    @refresh() if data.menuItem is "refresh"
                                    @goToCreate() if data.menuItem is "create"
        )
        # fetch data from server
        @listIndustries()
        
    listIndustries: () ->
        @$log.debug "IndustryCtrl.listIndustries()"
        @Industry.query().$promise
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Industries"
                @industries = data
            ,
            (error) =>
                @$log.error "Unable to get Industries: #{error}"
            )
    
    refresh: () ->
        @listIndustries()
    
    goToCreate: () ->
      @$log.debug "IndustryCtrl.goToCreate()"
      @$state.go("industryCreate")
    
    delete: (industry) ->
        @$log.debug "IndustryCtrl.delete(#{industry})"
        @Industry.remove({industryId: industry.industryId})
        @listIndustries()
    
    goToEdit: (industry) ->
        @$log.debug "IndustryCtrl.goToEdit(#{industry})"
        @$state.go("industryEdit", {industryId: industry.industryId})

controllersModule.controller('IndustryCtrl', IndustryCtrl)