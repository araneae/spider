
class SharedDatabaseCtrl

    constructor: (@$log, @$scope, @SharedDatabaseService, @$state, @$stateParams, @SharedDocument, @UtilityService) ->
        @$log.debug "constructing SharedDatabaseCtrl"
        @documents = []
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
        @$log.debug "SharedDatabaseCtrl.showRemoveAlert()"
            
controllersModule.controller('SharedDatabaseCtrl', SharedDatabaseCtrl)