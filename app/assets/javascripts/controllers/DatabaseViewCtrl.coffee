
class DatabaseViewCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q, @Document, 
                            @DatabaseService, @UtilityService, @$location, @ErrorService) ->
        @$log.debug "constructing DatabaseViewCtrl"
        @documentId = parseInt(@$stateParams.documentId)
        @document = {}
        @contents = ""
        # load objects from server
        @loadDocument(@documentId)
        @loadDocumentContents(@documentId)

    loadDocument: (documentId) ->
        @$log.debug "DatabaseViewCtrl.loadDocument(#{documentId})"
        #delay = @$q.defer()
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

    done: () ->
      @$log.debug "DatabaseViewCtrl.done()"
      @$state.go('database.documents')


controllersModule.controller('DatabaseViewCtrl', DatabaseViewCtrl)