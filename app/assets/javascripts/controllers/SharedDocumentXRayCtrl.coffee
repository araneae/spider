
class SharedDocumentXRayCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q,
                            @SharedRepositoryService, @UtilityService, @$location, @ErrorService) ->
        @$log.debug "constructing SharedDocumentXRayCtrl"
        @documentId = parseInt(@$stateParams.documentId)
        @document = {}
        @results = []
        # load objects from server
        @loadDocument(@documentId)
        @searchDocument(@documentId)

    loadDocument: (documentId) ->
        @$log.debug "SharedDocumentXRayCtrl.loadDocument(#{documentId})"
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

    searchDocument: (documentId) ->
        @$log.debug "SharedDocumentXRayCtrl.searchDocument(#{documentId})"
        #delay = @$q.defer()
        @SharedRepositoryService.searchDocument(documentId).then(
          (data) =>
              @$log.debug "Promise returned #{data} search results"
              @results = data
          ,
          (error) =>
              @ErrorService.error
              @$log.error "Unable to search document: #{error}"
          )

    done: () ->
      @$log.debug "SharedDocumentXRayCtrl.done()"
      @$state.go('sharedRepositories')


controllersModule.controller('SharedDocumentXRayCtrl', SharedDocumentXRayCtrl)