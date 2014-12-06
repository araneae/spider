
class JobTitleCtrl

    constructor: (@$log, @$scope, @$state, @JobTitleService, @JobTitle, @Industry, @ErrorService, @UtilityService, @$location) ->
        @$log.debug "constructing JobTitleController"
        @jobTitle = {}
        @jobTitles = []
        @industry = {}
        @industries = []
        @$scope.$on('contextMenu', (event, data) =>
                                    @$log.debug "received message contextMenu(#{data.menuItem})"
                                    @refresh() if data.menuItem is "refresh"
                                    @goToCreate() if data.menuItem is "create"
        )
        # fetch data from server
        @listIndustries()
        @listJobTitles()

    listIndustries: () ->
        @$log.debug "JobTitleCtrl.listIndustries()"
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
    
    listJobTitles: () ->
        @$log.debug "JobTitleCtrl.listJobTitles()"
        @jobTitle = {}
        @industry = {}
        @jobTitles = @JobTitle.query().$promise
          .then(
            (data) =>
              @$log.debug "Promise returned #{data.length} JobTitles"
              @jobTitles = data
            ,
            (error) =>
              @ErrorService.error
              @$log.error "Unable to get fetch JobTitles: #{error}"
          )

    refresh: () ->
        @listJobTitles()

    goToCreate: () ->
        @$log.debug "JobTitleCtrl.goToCreate()"
        @$state.go("jobTitleCreate")
    
    delete: (jobTitle) ->
        @$log.debug "JobTitleCtrl.delete(#{jobTitle.jobTitleId})"
        @JobTitle.remove({jobTitleId: jobTitle.jobTitleId})
        @listJobTitles()
    
    goToEdit: (jobTitle) ->
        @$log.debug "IndustryCtrl.goToEdit(#{jobTitle})"
        @$state.go("jobTitleEdit", {jobTitleId: jobTitle.jobTitleId})
        
controllersModule.controller('JobTitleCtrl', JobTitleCtrl)