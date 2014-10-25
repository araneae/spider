
class SharedDatabaseCtrl

    constructor: (@$log, @$scope, @ConnectionService, @$state, @$stateParams, @SharedDocument, @UtilityService, @ErrorService) ->
        @$log.debug "constructing SharedDatabaseCtrl"
        @documents = []
        @removeId
        @removeAlert = false
        # load list of documents from server
        @listDocuments()
    
    listDocuments: () ->
        @$log.debug "SharedDatabaseCtrl.listDocuments()"
        @SharedDocument.query().$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} documents"
                @documents = data
            ,
            (error) =>
                @$log.error "Unable to get documents: #{error}"
            )

    goToShare: (documentId) ->
        @$log.debug "SharedDatabaseCtrl.goToShare(#{documentId})"
        @$state.go("sharedDatabaseDocumentShare", {documentId: documentId})

    copyDocument: (documentId) ->
        @$log.debug "SharedDatabaseCtrl.copyDocument(#{documentId})"
        @ConnectionService.copyDocument(documentId).then(
            (data) =>
                # show status
                @$log.debug "Successfully copied document"
            ,
            (error) =>
                @$log.error "Unable to copy document: #{error}"
                @ErrorService.error
            )

    showRemoveAlert: (documentId) ->
        @$log.debug "SharedDatabaseCtrl.showRemoveAlert(#{documentId})"
        @removeId = documentId
        @removeAlert = true

    cancelRemove: () ->
        @$log.debug "SharedDatabaseCtrl.cancelRemove()"
        @removeAlert = false
        @removeId

    remove: () ->
        @$log.debug "SharedDatabaseCtrl.remove()"
        @removeAlert = false
        @SharedDocument.remove({documentId: @removeId}).$promise.then(
            (data) =>
                @$log.debug "Successfully deleted document!"
                @listDocuments()
            ,
            (error) =>
                @$log.error "Unable to delete document: #{error}!"
            )

controllersModule.controller('SharedDatabaseCtrl', SharedDatabaseCtrl)