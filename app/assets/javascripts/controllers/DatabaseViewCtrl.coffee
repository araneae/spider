
class DatabaseViewCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q, @Document, 
                            @DatabaseService, @UtilityService, @$location, @ErrorService) ->
        @$log.debug "constructing DatabaseViewCtrl"
        @documentFolderId = parseInt(@$stateParams.documentFolderId)
        @documentId = parseInt(@$stateParams.documentId)
        @document = {}
        @firstDocument = {}
        @previousDocument = {}
        @nextDocument = {}
        @lastDocument = {}
        @documents = []
        @contents = ""

        # load objects from server
        #@loadDocument(@documentId)
        @loadDocuments(@documentFolderId)
        @loadDocumentContents(@documentId)

      loadDocument: (documentId) ->
        @$log.debug "DatabaseViewCtrl.loadDocument(#{documentId})"
        @Document.get({documentId: documentId}).$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data} document"
              @document = data
          ,
          (error) =>
              @ErrorService.error("Unable to fetch data from server!")
              @$log.error "Unable to get document: #{error}"
          )

    loadDocumentContents: (documentId) ->
        @$log.debug "DatabaseViewCtrl.loadDocumentContents(#{documentId})"
        #delay = @$q.defer()
        
        @DatabaseService.getDocumentContents(documentId).then(
          (data) =>
              @contents = data
              @$log.debug "Promise returned #{data} document contents"
          ,
          (error) =>
              @ErrorService.error("Unable to fetch contents from the server!")
              @$log.error "Unable to fetch contents from the server: #{error}"
          )
    
    loadDocuments: (documentFolderId) ->
        @$log.debug "DatabaseViewCtrl.loadDocuments(#{documentFolderId})"
        #delay = @$q.defer()
        
        @DatabaseService.getDocumentByDocumentFolderId(documentFolderId).then(
          (data) =>
              @documents = data
              @$log.debug "Promise returned #{data.length} documents"
              if (@documents.length > 0)
                #@documents.sort((a, b) =>
                #                        a.documentId > b.documentId
                #  )
                index = @UtilityService.findIndexByProperty(@documents, "documentId", @documentId)
                @document = @documents[index] if index > -1
                @firstDocument = @documents[0]
                @previousDocument = @documents[index - 1] if index > 0
                @nextDocument = @documents[index + 1] if index < (@documents.length - 1)
                @lastDocument = @documents[@documents.length - 1]
          ,
          (error) =>
              @ErrorService.error("Unable to fetch documents from the server!")
              @$log.error "Unable to fetch documents from the server: #{error}"
          )
    
    showFirst: () ->
      @firstDocument.documentId isnt @documentId

    showPrevious: () ->
      @previousDocument.documentId
    
    showNext: () ->
      @nextDocument.documentId
    
    showLast: () ->
      @lastDocument.documentId isnt @documentId
    
    first: () ->
      @$log.debug "DatabaseViewCtrl.first()"
      @$state.go("databaseDocumentView", {folderId: @documentFolderId, documentId: @firstDocument.documentId}) if @firstDocument.documentId

    previous: () ->
      @$log.debug "DatabaseViewCtrl.previous()"
      @$state.go("databaseDocumentView", {folderId: @documentFolderId, documentId: @previousDocument.documentId}) if @previousDocument.documentId

    next: () ->
      @$log.debug "DatabaseViewCtrl.next()"
      @$state.go("databaseDocumentView", {folderId: @documentFolderId, documentId: @nextDocument.documentId}) if @nextDocument.documentId

    last: () ->
      @$log.debug "DatabaseViewCtrl.last()"
      @$state.go("databaseDocumentView", {folderId: @documentFolderId, documentId: @lastDocument.documentId}) if @lastDocument.documentId

    done: () ->
      @$log.debug "DatabaseViewCtrl.done()"
      @UtilityService.goBack('folder.documents')

controllersModule.controller('DatabaseViewCtrl', DatabaseViewCtrl)