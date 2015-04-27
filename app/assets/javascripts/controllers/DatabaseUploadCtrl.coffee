
class DatabaseUploadCtrl

    constructor: (@$log, @$state, @DatabaseService, @Document, @ErrorService, @DocumentFolder, @UtilityService, @ConfigService) ->
        @$log.debug "constructing DatabaseUploadCtrl"
        @folders = []
        @folder
        @fileUpload = {}
        @selectedFiles = []
        
        # load data from server
        @loadFolders()

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

    loadFolders: () ->
        @$log.debug "DatabaseUploadCtrl.loadFolders()"
        @DocumentFolder.query().$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data} folder"
              @folders = data.filter( (item) => item.shared is false)
              if (@folders && @folders.length > 0)
                @folder =  @folders[0]
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

    onUploadSuccess: (data, fileObj) =>
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
                #@UtilityService.goBack('folder.documents')
                fileObj.status = "Uploaded"
            ,
            (error) =>
                #@ErrorService.error("Failed to upload document!")
                @$log.error "Unable to save document: #{error.data.message}"
                fileObj.status = "Failed to upload"
            )

    onUploadError : (error, fileObj) =>
      @$log.debug "DatabaseUploadCtrl.onUploadError(#{error})"
      fileObj.status = "Upload failed"
    
    progressFn : (percent, fileObj) =>
      fileObj.status = percent

    cancel: () ->
        @$log.debug "DatabaseUploadCtrl.cancel()"
        @UtilityService.goBack('folder.documents')

controllersModule.controller('DatabaseUploadCtrl', DatabaseUploadCtrl)