
class JobSearchCtrl

    constructor: (@$log, @$scope, @$state, @JobRequirementService, @JobRequirement, @Industry,
                        @ErrorService, @UtilityService, @$location) ->
        @$log.debug "constructing JobRequirementController"
        @searchText = ""
        @locDetails
        @searchResults = []
        @jobSearch = {distance: 50}
        
        today = new Date()
        @date = @UtilityService.formatDate(@UtilityService.addDays(today, -14))
        
        # google places options
        @placesOptions = {
                country: '',
                types: 'geocode'
        }
        
        # fetch data from server

    clearSearchText: () ->
        @searchText = ""
        @search()
      
    search: () ->
        @$log.debug "JobSearchCtrl.search()"
        @searchResults = []
        if (@searchText)
          @jobSearch.contents = @searchText
          @jobSearch.date = @date
          @JobRequirementService.search(@jobSearch)
            .then(
              (data) =>
                #@$log.debug "Promise returned #{data.length} search results"
                @$log.debug "Promise returned #{angular.toJson(data)} search results"
                @searchResults = data
              ,
              (error) =>
                @ErrorService.error
                @$log.error "Unable to search : #{error}"
            )

    goToView: (job) ->
      @$state.go('jobRequirementView', {jobRequirementId: job.jobRequirementId})

controllersModule.controller('JobSearchCtrl', JobSearchCtrl)