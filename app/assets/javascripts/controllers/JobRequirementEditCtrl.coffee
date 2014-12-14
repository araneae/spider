
class JobRequirementEditCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams,  @Industry, @JobRequirement, @JobTitle, 
                    @EnumService, @ErrorService, @UtilityService, @$location) ->
        @$log.debug "constructing JobRequirementController"
        @jobRequirementId = parseInt(@$stateParams.jobRequirementId)
        @jobTitle = {}
        @jobTitles = []
        @industry = {}
        @industries = []
        @today = new Date()
        @dateOptions = {
           startingDay : 1,
           format : 'MM/dd/yyyy',
           minStartDate : @today,
           minEndDate : @today
        }
        @jobRequirement = {}
        @targetStartDate
        @targetEndDate
        @isTargetStartDatePickerOpened = false
        @isTargetEndDatePickerOpened = false
        
        # for watching property change
        @$scope.targetStartDate = () =>  
            @targetStartDate

        # watching for any change in "target start date" control
        @$scope.$watch('targetStartDate()', (newVal) =>
                      if (newVal)
                        @dateOptions.minEndDate = newVal
                      else  
                        @dateOptions.minEndDate = @today
                      )

        # fetch data from server
        @loadJobRequirement()
        @listJobTitles()
        @listIndustries()
    
    openStartDatePicker: (event) ->
      event.preventDefault()
      event.stopPropagation()
      @isTargetStartDatePickerOpened = true
      
    openEndDatePicker: (event) ->
      event.preventDefault()
      event.stopPropagation()
      @isTargetEndDatePickerOpened = true
      
    loadJobRequirement: () ->
        @$log.debug "JobRequirementEditCtrl.loadJobRequirement()"
        @jobRequirement = {}
        @JobRequirement.get({jobRequirementId: @jobRequirementId}).$promise.then(
              (data) =>
                @$log.debug "Promise returned #{data} Job Requirement"
                @jobRequirement = data
                @targetStartDate = @jobRequirement.xtn.targetStartDate if @jobRequirement.xtn.targetStartDate
                @targetEndDate = @jobRequirement.xtn.targetEndDate if @jobRequirement.xtn.targetEndDate
                @industries.filter( (item) => @industry = item if item.industryId is @jobRequirement.industryId) if @industries
                @jobTitles.filter( (item) => @jobTitle = item if item.jobTitleId is @jobRequirement.jobTitleId) if @jobTitles
              ,
              (error) =>
                @ErrorService.error
                @$log.error "Unable to fetch data from server: #{error}"
          )
    
    listIndustries: () ->
        @$log.debug "JobRequirementEditCtrl.listIndustries()"
        @Industry.query().$promise
          .then(
             (data) =>
                @$log.debug "Promise returned #{data.length} Industries"
                @industries = data
                @industries.filter( (item) => @industry = item if item.industryId is @jobRequirement.industryId) if @industries
             ,
             (error) =>
                @ErrorService.error
                @$log.error "Unable to get Industries: #{error}"
             )
  
    listJobTitles: () ->
        @$log.debug "JobRequirementEditCtrl.listJobTitles()"
        @JobTitle.query().$promise
          .then(
             (data) =>
                @$log.debug "Promise returned #{data.length} Job Titles"
                @jobTitles = data
                @jobTitles.filter( (item) => @jobTitle = item if item.jobTitleId is @jobRequirement.jobTitleId) if @jobTitles
             ,
             (error) =>
                @ErrorService.error
                @$log.error "Unable to get Job Titles: #{error}"
             )
             
    save: () ->
        @$log.debug "JobRequirementEditCtrl.save()"
        
        @jobRequirement.industryId = @industry.industryId
        @jobRequirement.jobTitleId = @jobTitle.jobTitleId
        @jobRequirement.xtn.targetStartDate = @UtilityService.formatDate(@targetStartDate)
        @jobRequirement.xtn.targetEndDate = @UtilityService.formatDate(@targetEndDate)
        
        @JobRequirement.update(@jobRequirement).$promise.then( 
              (data) =>
                @ErrorService.success("Successfully update jobRequirement #{@jobRequirement.title}")
                @$log.debug "server returned #{data} JobRequirement"
                @$state.go("jobRequirements")
             (error) =>
                @ErrorService.error("Oops, something wrong! Unable to update #{@jobRequirement.title}!")
                @$log.error "Unable to get Industries: #{error}"
        )

    cancel: () ->
        @$log.debug "JobRequirementEditCtrl.cancel()"
        @$state.go("jobRequirements")

controllersModule.controller('JobRequirementEditCtrl', JobRequirementEditCtrl)