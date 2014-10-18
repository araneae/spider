
class DatabaseShareCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q, @Document, 
                              @DatabaseService, @UtilityService, @$location, @ErrorService) ->
        @$log.debug "constructing DatabaseShareCtrl"
        @documentId = parseInt(@$stateParams.documentId)
        @document = {}
        @share = {}
        @share.canCopy = true
        @share.canShare = true
        @connections = []
        @sharedConnections = []
        @select2Options = {
           data : @connections,
           multiple: true
        }
        # load objects from server
        @loadDocument(@documentId)
        @loadConnections(@documentId)

    loadDocument: (documentId) ->
        @$log.debug "DatabaseShareCtrl.loadDocument(#{documentId})"
        #delay = @$q.defer()
        @Document.get({documentId: documentId}).$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data} document"
              @document = data
              @share.subject = "Shared a document \"#{@document.name}\""
          ,
          (error) =>
              @$log.error "Unable to get document: #{error}"
              @ErrorService.error("Unable to fetch data from server!")
          )

    loadConnections: (documentId) ->
        @$log.debug "DatabaseShareCtrl.loadConnections(#{documentId})"
        @DatabaseService.getConnections(documentId).then(
            (data) => 
              @$log.debug "Promise returned #{data} connections"
              for obj in data
                @connections.push(obj) if obj.shared == false
                @sharedConnections.push(obj) if obj.shared == true
            ,
            (error) =>
              @$log.error "Unable to get connections: #{error}"
              @ErrorService.error("Unable to fetch contacts from server!")
        )

    disableShare: () ->
      @UtilityService.isArrayEmpty(@share.receivers)

    sendShare: () ->
      @$log.debug "DatabaseShareCtrl.sendShare()"
      @DatabaseService.share(@documentId, @share).then(
            (data) => 
              @$log.debug "Promise returned #{data} connections"
              @ErrorService.success("Successfully shared document!")
              @$state.go('database.documents')
            ,
            (error) =>
              @$log.error "Unable to share connections: #{error}"
              @ErrorService.error("Unable to share document!")
      )

    cancel: () ->
      @$log.debug "DatabaseShareCtrl.cancel()"
      @$state.go('database.documents')

controllersModule.controller('DatabaseShareCtrl', DatabaseShareCtrl)