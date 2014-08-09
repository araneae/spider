
class SharedDatabaseShareCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q, @Document, @SharedDocument, @SharedDatabaseService, @UtilityService, @$location) ->
        @$log.debug "constructing SharedDatabaseShareCtrl"
        @documentId = parseInt(@$stateParams.documentId)
        @document = {}
        @sharedDocument = {}
        @share = {}
        @share.canCopy = true
        @share.canShare = true
        @connections = []
        @select2Options = {
           data : @connections,
           multiple: true
        }
        # load objects from server
        @loadDocument(@documentId)
        @loadSharedDocument(@documentId)
        @loadConnections()

    loadDocument: (documentId) ->
        @$log.debug "SharedDatabaseShareCtrl.loadDocument(#{documentId})"
        #delay = @$q.defer()
        @Document.get({documentId: documentId}).$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data} shared document"
              @document = data
          ,
          (error) =>
              @$log.error "Unable to get shared document: #{error}"
          )

    loadSharedDocument: (documentId) ->
        @$log.debug "SharedDatabaseShareCtrl.loadSharedDocument(#{documentId})"
        @SharedDocument.get({documentId: documentId}).$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} documents"
                @sharedDocument = data
                # assign the inherited attributes
                @share.canCopy = @sharedDocument.canCopy
                @share.canShare = @sharedDocument.canShare
            ,
            (error) =>
                @$log.error "Unable to get documents: #{error}"
            )

    loadConnections: () ->
        @$log.debug "SharedDatabaseShareCtrl.loadConnections()"
        @SharedDatabaseService.getConnections().then(
            (data) => 
              @$log.debug "Promise returned #{data} connections"
              for obj in data
                @connections.push(obj)
            ,
            (error) =>
              @$log.error "Unable to get connections: #{error}"
        )

    disableShare: () ->
      @UtilityService.isArrayEmpty(@share.receivers)
    
    sendShare: () ->
      @$log.debug "SharedDatabaseShareCtrl.sendShare()"
      @SharedDatabaseService.share(@documentId, @share).then(
            (data) => 
              @$log.debug "Promise returned #{data} connections"
              @$state.go('sharedDatabase')
            ,
            (error) =>
              @$log.error "Unable to share connections: #{error}"
      )

    cancel: () ->
      @$log.debug "SharedDatabaseShareCtrl.cancel()"
      @$state.go('sharedDatabase')

controllersModule.controller('SharedDatabaseShareCtrl', SharedDatabaseShareCtrl)