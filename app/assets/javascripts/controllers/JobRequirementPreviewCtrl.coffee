
class JobRequirementPreviewCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams,  @Industry, @JobRequirement, @JobRequirementService, @JobTitle,
                    @EnumService, @ErrorService, @UtilityService, @$location) ->
        @$log.debug "constructing JobRequirementPreviewCtrl"
        @jobRequirementId = parseInt(@$stateParams.jobRequirementId)
        @job = {}
        
        # fetch data from server
        @loadJobPreview()
      
    loadJobPreview: () ->
        @$log.debug "JobRequirementPreviewCtrl.loadJobPreview()"
        @job = {}
        @JobRequirementService.getPreview(@jobRequirementId).then(
              (data) =>
                @$log.debug "Promise returned #{data} Job Requirement"
                @job = data
              ,
              (error) =>
                @ErrorService.error
                @$log.error "Unable to fetch data from server: #{error}"
          )

    post: () ->
        @$log.debug "JobRequirementPreviewCtrl.post()"
        @JobRequirementService.post(@job.jobRequirementId).then(
              (data) =>
                @ErrorService.success(data.message)
                @$log.debug "Promise returned #{data} Job Requirement"
                @$state.go('jobRequirements')
              ,
              (error) =>
                @ErrorService.error
                @$log.error "Unable to fetch data from server: #{error}"
          )
    
    makeDraft: () ->
        @$log.debug "JobRequirementPreviewCtrl.post()"
        @JobRequirementService.makeDraft(@job.jobRequirementId).then(
              (data) =>
                @ErrorService.success(data.message)
                @$log.debug "Promise returned #{data} Job Requirement"
                @$state.go('jobRequirements')
              ,
              (error) =>
                @ErrorService.error
                @$log.error "Unable to fetch data from server: #{error}"
          )
    
    cancel: () ->
        @$log.debug "JobRequirementPreviewCtrl.cancel()"
        @$state.go("jobRequirements")

controllersModule.controller('JobRequirementPreviewCtrl', JobRequirementPreviewCtrl)