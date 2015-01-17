
class JobRequirementCtrl

    constructor: (@$log, @$scope, @$state, @JobRequirementService, @JobRequirement, @Industry, @ErrorService, @UtilityService, @$location) ->
        @$log.debug "constructing JobRequirementCtrl"
        @jobRequirement = {}
        @jobRequirements = []
        @industry = {}
        @industries = []
        @$scope.$on('contextMenu', (event, data) =>
                                    @$log.debug "received message contextMenu(#{data.menuItem})"
                                    @refresh() if data.menuItem is "refresh"
                                    @goToCreate() if data.menuItem is "create"
        )
        @deleteObj
        @deleteAlert = false
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
        @JobRequirement.query().$promise
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
    
    showDeleteAlert: (jobRequirement) ->
        @$log.debug "JobRequirementCtrl.showDeleteAlert(#{jobRequirement.jobRequirementId})"
        @deleteObj = jobRequirement
        @deleteAlert = true
        
    delete: () ->
        @$log.debug "JobRequirementCtrl.delete()"
        @deleteAlert = false
        @JobRequirement.remove({jobRequirementId: @deleteObj.jobRequirementId}).$promise
            .then(
                (data) =>
                  @$log.debug "Promise returned #{data}"
                  @ErrorService.success(data.message)
                  @listJobRequirements()
                ,
                (error) =>
                  @ErrorService.error("Unable to delete Job Requirement #{deleteObj.title}")
                  @$log.error "Unable to get delete JobRequirement: #{error}"
            )
    
    cancelDelete: () ->
        @$log.debug "JobRequirementCtrl.cancelDelete()"
        @deleteAlert = false
        @deleteObj
    
    preview: (jobRequirement) ->
        @$log.debug "JobRequirementCtrl.preview(#{jobRequirement.jobRequirementId})"
        @$state.go("jobRequirementPreview", {jobRequirementId: jobRequirement.jobRequirementId})

    goToEdit: (jobRequirement) ->
        @$log.debug "IndustryCtrl.goToEdit(#{jobRequirement})"
        @$state.go("jobRequirementEdit", {jobRequirementId: jobRequirement.jobRequirementId})

controllersModule.controller('JobRequirementCtrl', JobRequirementCtrl)