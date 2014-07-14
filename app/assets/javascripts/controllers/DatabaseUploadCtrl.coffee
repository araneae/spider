
class DatabaseUploadCtrl

    constructor: (@$log, @$state, @DatabaseService, @Document, @$upload) ->
        @$log.debug "constructing DatabaseUploadCtrl"
        @flashMessage
        @document = {}
        @fileUpload = {}
        @selectedFile

    onFileSelect: ($files) ->
      @$log.debug "DatabaseUploadCtrl.onFileSelect()"
      for file in $files
        name = @getName(file.name)
        @document.fileName = file.name
        @document.name = name
        @document.description = name 
        @selectedFile = file
        break

    save: () ->
        @$log.debug "DatabaseUploadCtrl.save()"
        @DatabaseService.upload(@selectedFile, @onUploadSuccess, @onUploadError)

    onUploadSuccess: (data, status, headers, config) =>
        @$log.debug "DatabaseUploadCtrl.onUploadSuccess()"
        @Document.save(@document).$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data} document"
                @$state.go('database.documents')
            ,
            (error) =>
                @$log.error "Unable to save document: #{error.data.message}"
                @flashMessage = error.data.message
            )

    onUploadError : (error) =>
      @$log.debug "DatabaseUploadCtrl.onUploadError(#{error.message})"
      @flashMessage = error.message

    cancel: () ->
        @$log.debug "DatabaseUploadCtrl.cancel()"
        @$state.go('database.documents')

    getName: (fileName) ->
      index = fileName.lastIndexOf('.')
      if (index > 0) 
        fileName.substring(0, index)
      else
        fileName

    showFlashMessage: () ->
       @flashMessage && @flashMessage.length > 0

controllersModule.controller('DatabaseUploadCtrl', DatabaseUploadCtrl)