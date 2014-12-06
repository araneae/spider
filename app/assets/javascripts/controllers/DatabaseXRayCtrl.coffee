
class DatabaseXRayCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q, @Document, 
                            @DatabaseService, @UtilityService, @$location, @ErrorService) ->
        @$log.debug "constructing DatabaseXRayCtrl"
        @documentId = parseInt(@$stateParams.documentId)
        @document = {}
        @results = []
        # load objects from server
        @loadDocument(@documentId)
        @searchDocument(@documentId)

    loadDocument: (documentId) ->
        @$log.debug "DatabaseXRayCtrl.loadDocument(#{documentId})"
        #delay = @$q.defer()
        @Document.get({documentId: documentId}).$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data} document"
              @document = data
          ,
          (error) =>
              @$log.error "Unable to get document: #{error}"
              @ErrorService.error("Unable to fetch data from server!")
          )

    searchDocument: (documentId) ->
        @$log.debug "DatabaseXRayCtrl.searchDocument(#{documentId})"
        #delay = @$q.defer()
        @DatabaseService.searchDocument(documentId).then(
          (data) =>
              @$log.debug "Promise returned #{data} search results"
              @results = data
          ,
          (error) =>
              @$log.error "Unable to search document: #{error}"
              @ErrorService.error("Unable to fetch data from server!")
          )

    done: () ->
      @$log.debug "DatabaseXRayCtrl.done()"
      @$state.go('database.documents')


controllersModule.controller('DatabaseXRayCtrl', DatabaseXRayCtrl)