
class SharedDatabaseCtrl

    constructor: (@$log, @$scope, @SharedDatabaseService, @$state, @$stateParams, @SharedDocument, @UtilityService) ->
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
        @$log.debug "SharedDatabaseCtrl.goToShare()"
    
    
    makeCopy: (documentId) ->
        @$log.debug "SharedDatabaseCtrl.makeCopy()"

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