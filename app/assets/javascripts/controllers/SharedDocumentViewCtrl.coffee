
class SharedDocumentViewCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q,
                            @SharedRepositoryService, @UtilityService, @$location, @ErrorService) ->
        @$log.debug "constructing SharedDocumentViewCtrl"
        @documentId = parseInt(@$stateParams.documentId)
        @document = {}
        @contents = ""
        # load objects from server
        @loadDocument(@documentId)
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

    done: () ->
      @$log.debug "SharedDocumentViewCtrl.done()"
      @$state.go('sharedRepositories')


controllersModule.controller('SharedDocumentViewCtrl', SharedDocumentViewCtrl)