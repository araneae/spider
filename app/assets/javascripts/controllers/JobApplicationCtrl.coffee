
class JobApplicationCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams,  @Industry, @JobRequirement, @JobRequirementService, @JobTitle,
                    @DocumentFolder, @DatabaseService, @EnumService, @ErrorService, @UtilityService, @$location) ->
        @$log.debug "constructing JobApplicationCtrl"
        @jobRequirementId = parseInt(@$stateParams.jobRequirementId)
        @job = {}
        @folders = []
        @folder
        @documents = []
        @document
        @weeks = [
                  {name: '1 Week', value: 1},
                  {name: '2 Weeks', value: 2},
                  {name: '3 Weeks', value: 3},
                  {name: '4 Weeks', value: 4}
                 ]
        @jobApplication = {availability: @UtilityService.findByProperty(@weeks, 'value', 2)}
        
        # for watching property change
        @$scope.refFolder = () =>
              @folder

        # watch for change
        @$scope.$watch('refFolder()', (newVal) =>
                            if (newVal)
                              @$log.debug "Changed folder #{angular.toJson(newVal)}"
                              @listDocuments(newVal.documentFolderId)
                      )

        # fetch data from server
        @loadJob()
        @loadFolders()

    loadJob: () ->
        @$log.debug "JobApplicationCtrl.loadJob()"
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
  
    loadFolders: () ->
      @$log.debug "JobApplicationCtrl.loadFolders()"
      @DocumentFolder.query().$promise.then(
        (data) =>
            @$log.debug "Promise returned #{data} folders"
            @folders = data
        ,
        (error) =>
            @ErrorService.error
            @$log.error "Unable to fetch folders from server: #{error}"
      )

    listDocuments: (documentFolderId) ->
        @$log.debug "JobApplicationCtrl.listDocuments()"
        @DatabaseService.getDocumentByDocumentFolderId(documentFolderId).then(
            (data) =>
                @$log.debug "Promise returned #{data.length} documents"
                @documents = data
            ,
            (error) =>
                @$log.error "Unable to get documents: #{error}"
            )

    apply: () ->
        @$log.debug "JobApplicationCtrl.apply()"
    
    cancel: () ->
        @$log.debug "JobApplicationCtrl.cancel()"
        @$state.go("jobSearch")

controllersModule.controller('JobApplicationCtrl', JobApplicationCtrl)