
class SharedDocumentViewCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q,
                            @SharedRepositoryService, @UtilityService, @$location, @ErrorService) ->
        @$log.debug "constructing SharedDocumentViewCtrl"
        @documentId = parseInt(@$stateParams.documentId)
        @document = {}
        @documents = []
        @firstDocument = {}
        @previousDocument = {}
        @nextDocument = {}
        @lastDocument = {}
                
        @index = 0
        @contents = ""
        # load objects from server
        #@loadDocument(@documentId)
        @loadDocuments()
        @loadDocumentContents(@documentId)

    loadDocument: (documentId) ->
        @$log.debug "SharedDocumentViewCtrl.loadDocument(#{documentId})"
        #delay = @$q.defer()
        @SharedRepositoryService.getDocument(documentId).then(
          (data) =>
              @$log.debug "Promise returned #{data} document"
              @document = data
          ,
          (error) =>
              @ErrorService.error
              @$log.error "Unable to get document: #{error}"
          )

    loadDocumentContents: (documentId) ->
        @$log.debug "SharedDocumentViewCtrl.loadDocumentContents(#{documentId})"
        #delay = @$q.defer()
        
        @SharedRepositoryService.getDocumentContents(documentId).then(
          (data) =>
              @contents = data
              @$log.debug "Promise returned #{data} document contents"
          ,
          (error) =>
              @ErrorService.error
              @$log.error "Unable to fetch contents from the server: #{error}"
          )
    
    loadDocuments: (documentFolderId) ->
        @$log.debug "DatabaseViewCtrl.loadDocuments(#{documentFolderId})"
        #delay = @$q.defer()
        
        @SharedRepositoryService.getDocuments().then(
          (data) =>
              @$log.debug "Promise returned #{data.length} documents"
              @documents = []
              for doc in data
                @documents.push(doc) if doc.canView

              if (@documents.length > 0)
                @index = @UtilityService.findIndexByProperty(@documents, "documentId", @documentId)
                @document = @documents[@index] if @index > -1
                @firstDocument = @documents[0]
                @previousDocument = @documents[@index - 1] if @index > 0
                @nextDocument = @documents[@index + 1] if @index < (@documents.length - 1)
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
      @$log.debug "SharedDocumentViewCtrl.first()"
      @$state.go("sharedDocumentView", {documentId: @firstDocument.documentId}) if @firstDocument.documentId

    previous: () ->
      @$log.debug "SharedDocumentViewCtrl.previous()"
      @$state.go("sharedDocumentView", {documentId: @previousDocument.documentId}) if @previousDocument.documentId

    next: () ->
      @$log.debug "SharedDocumentViewCtrl.next()"
      @$state.go("sharedDocumentView", {documentId: @nextDocument.documentId}) if @nextDocument.documentId

    last: () ->
      @$log.debug "SharedDocumentViewCtrl.last()"
      @$state.go("sharedDocumentView", {documentId: @lastDocument.documentId}) if @lastDocument.documentId

    done: () ->
      @$log.debug "SharedDocumentViewCtrl.done()"
      @$state.go('sharedRepositories')


controllersModule.controller('SharedDocumentViewCtrl', SharedDocumentViewCtrl)