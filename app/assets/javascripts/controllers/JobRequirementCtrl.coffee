
class JobRequirementCtrl

    constructor: (@$log, @$scope, @$state, @JobRequirementService, @JobRequirement, @Industry, @ErrorService, @UtilityService, @$location) ->
        @$log.debug "constructing JobRequirementController"
        @jobRequirement = {}
        @jobRequirements = []
        @industry = {}
        @industries = []
        @$scope.$on('contextMenu', (event, data) =>
                                    @$log.debug "received message contextMenu(#{data.menuItem})"
                                    @refresh() if data.menuItem is "refresh"
                                    @goToCreate() if data.menuItem is "create"
        )
        # fetch data from server
        @listIndustries()
        @listJobRequirements()

    listIndustries: () ->
        @$log.debug "JobRequirementCtrl.listIndustries()"
        @Industry.query().$promise
          .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Industries"
                @industries = data
            ,
            (error) =>
                @ErrorService.error
                @$log.error "Unable to get Industries: #{error}"
            )
    
    listJobRequirements: () ->
        @$log.debug "JobRequirementCtrl.listJobRequirements()"
        @jobRequirement = {}
        @industry = {}
        @jobRequirements = @JobRequirement.query().$promise
          .then(
            (data) =>
              @$log.debug "Promise returned #{data.length} JobRequirements"
              @jobRequirements = data
            ,
            (error) =>
              @ErrorService.error
              @$log.error "Unable to get fetch JobRequirements: #{error}"
          )

    refresh: () ->
        @listJobRequirements()

    goToCreate: () ->
        @$log.debug "JobRequirementCtrl.goToCreate()"
        @$state.go("jobRequirementCreate")
    
    delete: (jobRequirement) ->
        @$log.debug "JobRequirementCtrl.delete(#{jobRequirement.jobRequirementId})"
        @JobRequirement.remove({jobRequirementId: jobRequirement.jobRequirementId})
        @listJobRequirements()
    
    goToEdit: (jobRequirement) ->
        @$log.debug "IndustryCtrl.goToEdit(#{jobRequirement})"
        @$state.go("jobRequirementEdit", {jobRequirementId: jobRequirement.jobRequirementId})
        
controllersModule.controller('JobRequirementCtrl', JobRequirementCtrl)