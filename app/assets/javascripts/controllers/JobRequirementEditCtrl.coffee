
class JobRequirementEditCtrl

    constructor: (@$log, @$state, @$stateParams, @JobRequirement, @ErrorService, @$location) ->
        @$log.debug "constructing JobRequirementController"
        @jobRequirementId = parseInt(@$stateParams.jobRequirementId)
        @industry = {}
        @industries = []
        @jobRequirement = {}
        # fetch data from server
        @loadJobRequirement()

    loadJobRequirement: () ->
        @$log.debug "JobRequirementEditCtrl.loadJobRequirement()"
        @jobRequirement = {}
        @JobRequirement.get({jobRequirementId: @jobRequirementId}).$promise.then(
              (data) =>
                @$log.debug "Promise returned #{data.length} jobRequirements"
                @jobRequirement = data
                @industry = {name: @jobRequirement.industryName}
                @industries.push(@industry)
              ,
              (error) =>
                @ErrorService.error
                @$log.error "Unable to fetch data from server: #{error}"
          )
    
    save: () ->
        @$log.debug "JobRequirementEditCtrl.save()"
        jobRequirement = new @JobRequirement(@jobRequirement);
        jobRequirement.$update().then( 
              (data) =>
                @ErrorService.success("Successfully update jobRequirement #{@jobRequirement.name}")
                @$log.debug "server returned #{data} JobRequirement"
                @$state.go("jobRequirement")
             (error) =>
                @ErrorService.error("Oops, something wrong! Unable to update #{@jobRequirement.name}!")
                @$log.error "Unable to get Industries: #{error}"
        )

    cancel: () ->
        @$log.debug "JobRequirementEditCtrl.cancel()"
        @$state.go("jobRequirement")

controllersModule.controller('JobRequirementEditCtrl', JobRequirementEditCtrl)