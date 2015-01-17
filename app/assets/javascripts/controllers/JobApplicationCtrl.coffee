
class JobApplicationCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams,  @Industry, @JobRequirement, @JobRequirementService, @JobTitle,
                    @DocumentFolder, @DatabaseService, @EnumService, @ErrorService, @UtilityService, @$location) ->
        @$log.debug "constructing JobApplicationCtrl"
        @jobRequirementId = parseInt(@$stateParams.jobRequirementId)
        @jobDef = {}
        @folders = []
        @attachments = [{label: 'Attachments', folders: @folders}]
        @weeks = [
                  {name: '1 Week', value: 1},
                  {name: '2 Weeks', value: 2},
                  {name: '3 Weeks', value: 3},
                  {name: '4 Weeks', value: 4}
                 ]
        @availability = @UtilityService.findByProperty(@weeks, 'value', 2)
        @jobApplication = {jobRequirementId: @jobRequirementId}
        
        # fetch data from server
        @loadJob()
        @loadFolders()

    loadJob: () ->
        @$log.debug "JobApplicationCtrl.loadJob()"
        @job = {}
        @JobRequirementService.getPreview(@jobRequirementId).then(
              (data) =>
                @$log.debug "Promise returned #{data} Job Requirement"
                @jobDef = data
              ,
              (error) =>
                @ErrorService.error
                @$log.error "Unable to fetch data from server: #{error}"
          )
  
    loadFolders: () ->
      @$log.debug "JobApplicationCtrl.loadFolders()"
      @DocumentFolder.query().$promise.then(
        (data) =>
            @$log.debug "Promise returned #{data.length} folders"
            for folder in data
              folder.documents = []
              @folders.push(folder)
        ,
        (error) =>
            @ErrorService.error
            @$log.error "Unable to fetch folders from server: #{error}"
      )

    folderChange: (attachment) ->
      @$log.debug "JobApplicationCtrl.folderChange(#{attachment})"
      if (attachment.folder.documents.length is 0)
         @loadDocuments(attachment.folder)

    loadDocuments: (folder) ->
        @$log.debug "JobApplicationCtrl.loadDocuments(#{folder})"
        @DatabaseService.getDocumentByDocumentFolderId(folder.documentFolderId).then(
            (data) =>
                @$log.debug "Promise returned #{data.length} documents"
                folder.documents = data
            ,
            (error) =>
                @ErrorService.error
                @$log.error "Unable to get documents: #{error}"
            )

    addAttachment: () ->
        @$log.debug "JobApplicationCtrl.addAttachment()"
        attachment = {folders: @folders}
        @attachments.push(attachment)
    
    removeAttachment: (index) ->
        @$log.debug "JobApplicationCtrl.removeAttachment(#{index})"
        @attachments.splice(index, 1)
    
    apply: () ->
        @$log.debug "JobApplicationCtrl.apply()"
        @jobApplication.availableInWeeks = @availability.value
        @jobApplication.companyId = @jobDef.companyId
        attachments = []
        for attachment in @attachments
          attachments.push({
                            attachmentType: attachment.attachmentType,
                            documentId: attachment.document.documentId})
        @jobApplication.attachments = attachments
        @JobRequirementService.apply(@jobRequirementId, @jobApplication).then(
            (data) =>
                @$log.debug "Promise returned #{data}"
                @ErrorService.success("Congratulation! Your job application was submitted successfully!")
                @$state.go('jobSearch')
            ,
            (error) =>
                @ErrorService.error("Oops! Something went wrong, unable to submit your job application!")
                @$log.error "Unable to submit application: #{error}"
            )

    cancel: () ->
        @$log.debug "JobApplicationCtrl.cancel()"
        @$state.go("jobSearch")

controllersModule.controller('JobApplicationCtrl', JobApplicationCtrl)