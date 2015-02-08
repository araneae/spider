
class DatabaseEditCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q, @Document, @UtilityService, @$location, @ErrorService) ->
        @$log.debug "constructing DatabaseEditCtrl"
        @documentId = parseInt(@$stateParams.documentId)
        @document = {}
        # load objects from server
        @loadDocument(@documentId)

    loadDocument: (documentId) ->
        @$log.debug "DatabaseEditCtrl.loadDocument(#{documentId})"
        delay = @$q.defer()
        @Document.get({documentId: documentId}).$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data} document"
              @document = data
          ,
          (error) =>
              @$log.error "Unable to get document: #{error}"
              @ErrorService.error("Unable to fetch data from server!")
          )

    cancel: () ->
      @$log.debug "DatabaseEditCtrl.cancel()"
      @$state.go('folder.documents')

    save: () ->
      @$log.debug "DatabaseEditCtrl.save()"
      @Document.save(@document).$promise.then(
        (data) =>
          @$log.debug "Successfully updated document!"
          @ErrorService.success("Successfully updated document!")
          @$state.go('folder.documents')
        ,
        (error) =>
          @$log.error "Unable to update document: #{error}"
          @ErrorService.error("Unable to update document!")
      )

controllersModule.controller('DatabaseEditCtrl', DatabaseEditCtrl)