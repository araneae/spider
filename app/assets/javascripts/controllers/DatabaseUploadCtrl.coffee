
class DatabaseUploadCtrl

    constructor: (@$log, @$state, @$stateParams, @DatabaseService, @Document, @ErrorService, @DocumentFolder, @UtilityService, @ConfigService) ->
        @$log.debug "constructing DatabaseUploadCtrl"
        @folder
        @selectedFiles = []
        @statusUploaded = "Uploaded"
        @statusUploadFailed = "Upload Failed"
        @documentFolderId = @$stateParams.documentFolderId
        @title = {}
        
        # load data from server
        @loadFolder(@documentFolderId)

    onFileSelect: ($files) ->
      @$log.debug "DatabaseUploadCtrl.onFileSelect()"
      @selectedFiles = []
      for file in $files
        name = @UtilityService.getFileName(file.name)
        fileObj = {
                      fileName: file.name,
                      name: name,
                      status: "Ready",
                      file: file
                    } 
        @selectedFiles.push(fileObj)
        break if @selectedFiles.length is @ConfigService.multiFileUploadLimit

    loadFolder: (documentFolderId) ->
        @$log.debug "DatabaseUploadCtrl.loadFolder(#{documentFolderId})"
        @DocumentFolder.get({documentFolderId: documentFolderId}).$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data} folder"
              @folder = data
              @title = "Uploading in #{data.name}"
          ,
          (error) =>
              @$log.error "Unable to fetch folder: #{error}"
              @ErrorService.error("Unable to fetch folder from server!")
          )
          
    save: () ->
        @$log.debug "DatabaseUploadCtrl.save()"
        for obj in @selectedFiles
          (() =>
              fileObj = obj
              @UtilityService.uploadFile('/database/upload',
                                  'application/text',
                                  fileObj.file,
                                  (data, status, headers, config) =>
                                      @onUploadSuccess(data, fileObj)
                                  ,
                                  (error) =>
                                      @onUploadError(error, fileObj)
                                  ,
                                  (percent) =>
                                      @progressFn(percent, fileObj))
          )()

    onUploadSuccess: (data, fileObj) ->
        @$log.debug "DatabaseUploadCtrl.onUploadSuccess()"
        document = {
                      fileName: fileObj.fileName,
                      name: fileObj.name,
                      description: fileObj.name,
                      documentFolderId: @folder.documentFolderId
                    }
        @Document.save(document).$promise.then(
            (data) =>
                @ErrorService.success("Successfully uploaded document!")
                @$log.debug "Promise returned #{data} document"
                fileObj.status = @statusUploaded
                @UtilityService.goBack('folder.documents') if @hasUploadedAll()
            ,
            (error) =>
                #@ErrorService.error("Failed to upload document!")
                @$log.error "Unable to save document: #{error.data.message}"
                fileObj.status = @statusUploadFailed
            )

    onUploadError : (error, fileObj) ->
      @$log.debug "DatabaseUploadCtrl.onUploadError(#{error})"
      fileObj.status = "Upload failed"

    hasUploadedAll: () ->
      for obj in @selectedFiles
        return false if obj.status isnt @statusUploaded
      return true
    
    progressFn : (percent, fileObj) ->
      fileObj.status = percent

    cancel: () ->
        @$log.debug "DatabaseUploadCtrl.cancel()"
        @UtilityService.goBack('folder.documents')

controllersModule.controller('DatabaseUploadCtrl', DatabaseUploadCtrl)