
class JobTitleCreateCtrl

    constructor: (@$log, @$scope, @$state, @JobTitleService, @JobTitle, @Industry, @ErrorService, @$location) ->
        @$log.debug "constructing JobTitleCreateCtrl"
        @jobTitle = {}
        @industry = {}
        @industries = []
        # fetch data from server
        @listIndustries()

    listIndustries: () ->
        @$log.debug "JobTitleCreateCtrl.listIndustries()"
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
        @$log.debug "JobTitleCreateCtrl.save()"
        @jobTitle.industryId = @industry.industryId
        jobTitle = new @JobTitle(@jobTitle);
        jobTitle.$save().then( 
          (data) =>
            @$log.debug "server returned #{data} JobTitle"
            @$state.go("jobTitle")
          ,
          (error) =>
            @ErrorService.error("Oops, something wrong! Unable to create #{@jobTitle.name}")
            @$log.error "Unable to create Industry: #{error}"
        )

    cancel: () ->
        @$log.debug "JobTitleCreateCtrl.cancel()"
        @$state.go("jobTitle")

controllersModule.controller('JobTitleCreateCtrl', JobTitleCreateCtrl)