
class JobTitleEditCtrl

    constructor: (@$log, @$state, @$stateParams, @JobTitle, @ErrorService, @$location) ->
        @$log.debug "constructing JobTitleController"
        @jobTitleId = parseInt(@$stateParams.jobTitleId)
        @industry = {}
        @industries = []
        @jobTitle = {}
        # fetch data from server
        @loadJobTitle()

    loadJobTitle: () ->
        @$log.debug "JobTitleEditCtrl.loadJobTitle()"
        @jobTitle = {}
        @JobTitle.get({jobTitleId: @jobTitleId}).$promise.then(
              (data) =>
                @$log.debug "Promise returned #{data.length} jobTitles"
                @jobTitle = data
                @industry = {name: @jobTitle.industryName}
                @industries.push(@industry)
              ,
              (error) =>
                @ErrorService.error
                @$log.error "Unable to fetch data from server: #{error}"
          )
    
    save: () ->
        @$log.debug "JobTitleEditCtrl.save()"
        jobTitle = new @JobTitle(@jobTitle);
        jobTitle.$update().then( 
              (data) =>
                @ErrorService.success("Successfully update jobTitle #{@jobTitle.name}")
                @$log.debug "server returned #{data} JobTitle"
                @$state.go("jobTitle")
             (error) =>
                @ErrorService.error("Oops, something wrong! Unable to update #{@jobTitle.name}!")
                @$log.error "Unable to get Industries: #{error}"
        )

    cancel: () ->
        @$log.debug "JobTitleEditCtrl.cancel()"
        @$state.go("jobTitle")

controllersModule.controller('JobTitleEditCtrl', JobTitleEditCtrl)