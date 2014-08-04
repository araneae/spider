
class DatabaseShareCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q, @Document, @DatabaseService, @UtilityService, @$location) ->
        @$log.debug "constructing DatabaseShareCtrl"
        @documentId = parseInt(@$stateParams.documentId)
        @document = {}
        @share = {}
        @share.canEdit = false
        @connections = []
        @select2Options = {
           data : @connections,
           multiple: true
        }
        # load objects from server
        @loadDocument(@documentId)
        @loadConnections()

    loadDocument: (documentId) ->
        @$log.debug "DatabaseShareCtrl.loadDocument(#{documentId})"
        #delay = @$q.defer()
        @Document.get({id: documentId}).$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data} document"
              @document = data
          ,
          (error) =>
              @$log.error "Unable to get document: #{error}"
          )

    loadConnections: () ->
        @$log.debug "DatabaseShareCtrl.loadConnections()"
        @DatabaseService.getInNetworkConnections().then(
            (data) => 
              @$log.debug "Promise returned #{data} connections"
              for obj in data
                @connections.push(obj)
            ,
            (error) =>
              @$log.error "Unable to get connections: #{error}"
        )

    disableSend: () ->
      @UtilityService.isArrayEmpty(@share.receivers)
    
    send: () ->
      @$log.debug "DatabaseShareCtrl.done()"
      @DatabaseService.shareInNetworkConnections(@documentId, @share).then(
            (data) => 
              @$log.debug "Promise returned #{data} connections"
              @$state.go('database.documents')
            ,
            (error) =>
              @$log.error "Unable to share connections: #{error}"
      )

    cancel: () ->
      @$log.debug "DatabaseShareCtrl.cancel()"
      @$state.go('database.documents')

controllersModule.controller('DatabaseShareCtrl', DatabaseShareCtrl)