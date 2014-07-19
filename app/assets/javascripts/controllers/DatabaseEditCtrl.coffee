
class DatabaseEditCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q, @Document, @UtilityService, @$location) ->
        @$log.debug "constructing DatabaseEditCtrl"
        @documentId = parseInt(@$stateParams.documentId)
        @document = {}
        # load objects from server
        @loadDocument(@documentId)

    loadDocument: (documentId) ->
        @$log.debug "DatabaseEditCtrl.loadDocument(#{documentId})"
        delay = @$q.defer()
        @Document.get({id: documentId}).$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data} document"
              @document = data
          ,
          (error) =>
              @$log.error "Unable to get document: #{error}"
          )

    cancel: () ->
      @$log.debug "DatabaseEditCtrl.cancel()"
      @$state.go('database.documents')

    save: () ->
      @$log.debug "DatabaseEditCtrl.save()"
      @Document.save(@document).$promise.then(
        (data) =>
          @$log.debug "Successfully updated document!"
          @$state.go('database.documents')
        ,
        (error) =>
          @$log.error "Unable to update document: #{error}"
      )

controllersModule.controller('DatabaseEditCtrl', DatabaseEditCtrl)