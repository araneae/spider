
class SharedRepositoryCtrl

    constructor: (@$log, @$scope, @SharedRepositoryService, @$state, @$stateParams,
                          @UtilityService, @ErrorService, @ConfigService) ->
        @$log.debug "constructing SharedRepositoryCtrl"
        @documents = []
        @displayCollection = []
        @searchText
        
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
                @displayCollection = angular.copy(data)
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

    goToShare: (documentId) ->
        @$log.debug "SharedRepositoryCtrl.goToShare(#{documentId})"
        @$state.go("sharedDocumentShare", {documentId: documentId})

    getDownloadUrl: (documentId) ->
        "/database/download/#{documentId}"

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
    
    clearSearchText: () ->
        @searchText = ""
        @search()

    search: () ->
        @$log.debug "SharedRepositoryCtrl.search()"
        if (@searchText)
          @documents = []
          @SharedRepositoryService.search(@searchText).then(
            (data) =>
                @$log.debug "Successfully returned search result #{data.length}"
                @documents = data
            ,
            (error) =>
                @ErrorService.error("Oops! Unable to search.")
                @$log.error "Unable to search #{searchText}"
            )
        else
          @listDocuments()
    
controllersModule.controller('SharedRepositoryCtrl', SharedRepositoryCtrl)