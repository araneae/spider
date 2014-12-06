
class JobRequirementCreateCtrl

    constructor: (@$log, @$scope, @$state, @JobRequirementService, @JobRequirement, @Industry, @EnumService, @ErrorService, @$location) ->
        @$log.debug "constructing JobRequirementCreateCtrl"
        @jobRequirement = {}
        @industry = {}
        @industries = []
        @jobRequirement.currencyType="USD"
        @jobRequirement.positions=1
        @jobRequirement.salaryTerm="WEEK"
        @jobRequirement.employmentType="CONTRACT"
        # fetch data from server
        @listIndustries()

    listIndustries: () ->
        @$log.debug "JobRequirementCreateCtrl.listIndustries()"
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
    
    save: () ->
        @$log.debug "JobRequirementCreateCtrl.save()"
        @jobRequirement.industryId = @industry.industryId
        jobRequirement = new @JobRequirement(@jobRequirement);
        jobRequirement.$save().then( 
          (data) =>
            @$log.debug "server returned #{data} JobRequirement"
            @$state.go("jobRequirement")
          ,
          (error) =>
            @ErrorService.error("Oops, something wrong! Unable to create #{@jobRequirement.name}")
            @$log.error "Unable to create Industry: #{error}"
        )

    cancel: () ->
        @$log.debug "JobRequirementCreateCtrl.cancel()"
        @$state.go("jobRequirement")

controllersModule.controller('JobRequirementCreateCtrl', JobRequirementCreateCtrl)