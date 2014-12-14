
class JobRequirementCreateCtrl

    constructor: (@$log, @$scope, @$state, @JobRequirementService, @JobRequirement, @Industry, @JobTitle, 
                          @EnumService, @ErrorService, @UtilityService, @$location) ->
        @$log.debug "constructing JobRequirementCreateCtrl"
        @industry = {}
        @industries = []
        @jobTitle = {}
        @jobTitles = []
        @today = new Date()
        @dateOptions = {
           startingDay : 1,
           format : 'MM/dd/yyyy',
           minStartDate : @today,
           minEndDate : @today
        }
        @targetStartDate
        @targetEndDate
        @jobRequirement = {}
        @jobRequirement.xtn = {}
        @jobRequirement.xtn.currency="USD"
        @jobRequirement.positions=1
        @jobRequirement.employmentType="CONTRACT"
        @jobRequirement.status="DRAFT"
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
        @listIndustries()
        @listJobTitles()

    openStartDatePicker: (event) ->
      event.preventDefault()
      event.stopPropagation()
      @isTargetStartDatePickerOpened = true
      
    openEndDatePicker: (event) ->
      event.preventDefault()
      event.stopPropagation()
      @isTargetEndDatePickerOpened = true
  
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
  
    listJobTitles: () ->
        @$log.debug "JobRequirementCreateCtrl.listJobTitles()"
        @JobTitle.query().$promise
          .then(
             (data) =>
                @$log.debug "Promise returned #{data.length} Job Titles"
                @jobTitles = data
             ,
             (error) =>
                @ErrorService.error
                @$log.error "Unable to get Job Titles"
             )
    
    save: () ->
        @$log.debug "JobRequirementCreateCtrl.save()"
        @jobRequirement.industryId = @industry.industryId
        @jobRequirement.jobTitleId = @jobTitle.jobTitleId
        @jobRequirement.xtn.targetStartDate = @UtilityService.formatDate(@targetStartDate) if @targetStartDate
        @jobRequirement.xtn.targetStartDate = @UtilityService.formatDate(@targetEndDate) if @targetEndDate

        @JobRequirement.save(@jobRequirement).$promise.then( 
          (data) =>
            @$log.debug "server returned #{data} JobRequirement"
            @$state.go("jobRequirements")
          ,
          (error) =>
            @ErrorService.error("Oops, something wrong! Unable to create #{@jobRequirement.title}")
            @$log.error "Unable to create job requirement: #{error}"
        )

    cancel: () ->
        @$log.debug "JobRequirementCreateCtrl.cancel()"
        @$state.go("jobRequirements")
    
    goToAddJobTitle: () ->
        @$log.debug "JobRequirementCreateCtrl.goToAddJobTitle()"
        @$state.go("jobTitleCreate")

controllersModule.controller('JobRequirementCreateCtrl', JobRequirementCreateCtrl)