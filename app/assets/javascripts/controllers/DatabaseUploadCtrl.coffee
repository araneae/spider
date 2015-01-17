
class DatabaseUploadCtrl

    constructor: (@$log, @$state, @DatabaseService, @Document, @ErrorService, @DocumentFolder, @UtilityService) ->
        @$log.debug "constructing DatabaseUploadCtrl"
        @folders = []
        @folder
        @document = {}
        @fileUpload = {}
        @selectedFile
        
        # load data from server
        @loadFolders()

    onFileSelect: ($files) ->
      @$log.debug "DatabaseUploadCtrl.onFileSelect()"
      for file in $files
        name = @UtilityService.getFileName(file.name)
        @document.fileName = file.name
        @document.name = name
        @document.description = name 
        @selectedFile = file
        break

    loadFolders: () ->
        @$log.debug "DatabaseUploadCtrl.loadFolders()"
        @DocumentFolder.query().$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data} folder"
              @folders = data
              if (data && data.length > 0)
                @folder =  data[0]
          ,
          (error) =>
              @$log.error "Unable to fetch folder: #{error}"
              @ErrorService.error("Unable to fetch folder from server!")
          )

    save: () ->
        @$log.debug "DatabaseUploadCtrl.save()"
        @UtilityService.uploadFile('/database/upload', 'application/text', @selectedFile, @onUploadSuccess, @onUploadError)

    onUploadSuccess: (data, status, headers, config) =>
        @$log.debug "DatabaseUploadCtrl.onUploadSuccess()"
        @document.documentFolderId = @folder.documentFolderId
        @Document.save(@document).$promise.then(
            (data) =>
                @ErrorService.success("Successfully uploaded document!")
                @$log.debug "Promise returned #{data} document"
                @UtilityService.goBack('folder.documents')
            ,
            (error) =>
                @ErrorService.error("Failed to upload document!")
                @$log.error "Unable to save document: #{error.data.message}"
            )

    onUploadError : (error) =>
      @ErrorService.error("Failed to upload document!")
      @$log.debug "DatabaseUploadCtrl.onUploadError(#{error})"

    cancel: () ->
        @$log.debug "DatabaseUploadCtrl.cancel()"
        @UtilityService.goBack('folder.documents')

controllersModule.controller('DatabaseUploadCtrl', DatabaseUploadCtrl)