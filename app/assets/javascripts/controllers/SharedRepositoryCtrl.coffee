
class SharedRepositoryCtrl

    constructor: (@$log, @$scope, @SharedRepositoryService, @$state, @$stateParams, @UtilityService, @ErrorService) ->
        @$log.debug "constructing SharedRepositoryCtrl"
        @documents = []
        
        @$scope.$on('globalSearch', (event, data) =>
                                    @$log.debug "received message globalSearch(#{data.searchText})"
                                    @search(data.searchText)
        )
        @$scope.$on('contextMenu', (event, data) =>
                                    @$log.debug "received message contextMenu(#{data.menuItem})"
                                    @refresh() if data.menuItem is "refresh"
        )
        
        # load list of documents from server
        @listDocuments()
    
    listDocuments: () ->
        @$log.debug "SharedRepositoryCtrl.listDocuments()"
        @SharedRepositoryService.getDocuments().then(
            (data) =>
                @$log.debug "Promise returned #{data.length} documents"
                @documents = data
            ,
            (error) =>
                @ErrorService.error
                @$log.error "Unable to get documents: #{error}"
            )

    refresh : () ->
      @listDocuments()

    goToXRay: (documentId) ->
        @$log.debug "SharedRepositoryCtrl.goToXRay(#{documentId})"
        @$state.go("sharedDocumentXRay", {documentId: documentId})

    goToView: (documentId) ->
        @$log.debug "SharedRepositoryCtrl.goToView(#{documentId})"
        @$state.go("sharedDocumentView", {documentId: documentId})

    copyDocument: (documentId) ->
        @$log.debug "SharedRepositoryCtrl.copyDocument(#{documentId})"
        @SharedRepositoryService.copyDocument(documentId).then(
            (data) =>
                # show status
                @$log.debug "Successfully copied document"
                @ErrorService.success("Successfully copied document.")
                @listDocuments()
            ,
            (error) =>
                @ErrorService.error("Oops! Unable to copy document.")
                @$log.error "Unable to copy document: #{error}"
            )
    
controllersModule.controller('SharedRepositoryCtrl', SharedRepositoryCtrl)