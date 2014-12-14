
class JobTitleCreateCtrl

    constructor: (@$log, @$scope, @$state, @JobTitleService, @JobTitle, @ErrorService, @$location) ->
        @$log.debug "constructing JobTitleCreateCtrl"
        @jobTitle = {}
        # fetch data from server

    save: () ->
        @$log.debug "JobTitleCreateCtrl.save()"
        jobTitle = new @JobTitle(@jobTitle);
        jobTitle.$save().then( 
          (data) =>
            @$log.debug "server returned #{data} JobTitle"
            @$state.go("jobTitles")
          ,
          (error) =>
            @ErrorService.error("Oops, something wrong! Unable to create #{@jobTitle.name}")
            @$log.error "Unable to create JobTitle: #{error}"
        )

    cancel: () ->
        @$log.debug "JobTitleCreateCtrl.cancel()"
        @$state.go("jobTitles")

controllersModule.controller('JobTitleCreateCtrl', JobTitleCreateCtrl)