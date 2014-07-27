
class DatabaseXRayCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q, @Document, @DatabaseService, @UtilityService, @$location) ->
        @$log.debug "constructing DatabaseXRayCtrl"
        @documentId = parseInt(@$stateParams.documentId)
        @document = {}
        @results = []
        @termsXRay = 'java or hadoop or oracle or mobile or microsoft or \"big data\"'
        # load objects from server
        @loadDocument(@documentId)
        @searchDocument(@documentId)

    loadDocument: (documentId) ->
        @$log.debug "DatabaseXRayCtrl.loadDocument(#{documentId})"
        #delay = @$q.defer()
        @Document.get({id: documentId}).$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data} document"
              @document = data
          ,
          (error) =>
              @$log.error "Unable to get document: #{error}"
          )

    searchDocument: (documentId) ->
        @$log.debug "DatabaseXRayCtrl.searchDocument(#{documentId})"
        #delay = @$q.defer()
        @DatabaseService.searchDocument(documentId, @termsXRay).then(
          (data) =>
              @$log.debug "Promise returned #{data} search results"
              @results = data
          ,
          (error) =>
              @$log.error "Unable to search document: #{error}"
          )

    done: () ->
      @$log.debug "DatabaseXRayCtrl.done()"
      @$state.go('database.documents')


controllersModule.controller('DatabaseXRayCtrl', DatabaseXRayCtrl)