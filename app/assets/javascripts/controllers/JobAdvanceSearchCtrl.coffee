
class JobAdvanceSearchCtrl

    constructor: (@$log, @$scope, @$state, @JobRequirementService, @JobRequirement, @Industry,
                        @ErrorService, @UtilityService, @$location) ->
        @$log.debug "constructing JobRequirementController"
        @jobRequirements = []
        @jobSearch = {distance: 50}
        @searchResults = []
        @locDetails
        
        today = new Date()
        minDate = @UtilityService.addDays(today, -60)
        maxDate = @UtilityService.addDays(today, -7)
        @dateOptions = {
           startingDay : -7,
           format : 'MM/dd/yyyy',
           minDate : minDate,
           maxDate : maxDate
        }
        @isDatePickerOpened = false
        @postDate = minDate
        
        # google places options
        @placesOptions = {
                country: '',
                types: 'geocode'
        }
        
        # fetch data from server

    openDatePicker: (event, contact) ->
      event.preventDefault()
      event.stopPropagation()
      @isDatePickerOpened = true

    search: () ->
        @$log.debug "JobAdvanceSearchCtrl.search()"
        @searchResults = []
        @jobSearch.date = @UtilityService.formatDate(@postDate)
        if (@locDetails)
          @jobSearch.locationLat = @locDetails.lat
          @jobSearch.locationLng = @locDetails.lng
        @JobRequirementService.search(@jobSearch)
          .then(
            (data) =>
              @$log.debug "Promise returned #{data.length} search results"
              @searchResults = data
            ,
            (error) =>
              @ErrorService.error
              @$log.error "Unable to search : #{error}"
          )

controllersModule.controller('JobAdvanceSearchCtrl', JobAdvanceSearchCtrl)