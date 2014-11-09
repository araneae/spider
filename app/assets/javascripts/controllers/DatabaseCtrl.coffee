
class DatabaseCtrl

    constructor: (@$log, @$scope, @DatabaseService, @$state, @$stateParams, @Document, @UtilityService, @ErrorService) ->
        @$log.debug "constructing DatabaseCtrl"
        @userTagId
        @userTagId = parseInt(@$stateParams.userTagId) if @$stateParams.userTagId
        @userTagId = 0 if !@userTagId
        @documents = []
        @removeId
        @removeAlert = false
        @$scope.$on('globalSearch', (event, data) =>
                                    @$log.debug "received message globalSearch(#{data.searchText})"
                                    @search(data.searchText)
        )
        @$scope.$on('contextMenu', (event, data) =>
                                    @$log.debug "received message contextMenu(#{data.menuItem})"
                                    @refresh() if data.menuItem is "refresh"
                                    @goToUpload() if data.menuItem is "uploadFile"
                                    @goToSearch() if data.menuItem is "advancedSearch"
        )
        # load list of documents from server
        @listDocuments()
    
    listDocuments: () ->
        @$log.debug "DatabaseCtrl.listDocuments()"
        @DatabaseService.getDocumentByUserTagId(@userTagId).then(
            (data) =>
                @$log.debug "Promise returned #{data.length} documents"
                @documents = data
                # update scope to refresh ui
                #@$scope.$apply()
            ,
            (error) =>
                @$log.error "Unable to get documents: #{error}"
            )
    
    refresh: () ->
      @listDocuments()
    
    goToDocumentTag: (documentId) ->
        @$log.debug "DatabaseCtrl.goToDocumentTag(#{documentId})"
        @$state.go("databaseDocumentTag", {documentId: documentId})

    goToDocumentEdit: (documentId) ->
        @$log.debug "DatabaseCtrl.goToDocumentEdit(#{documentId})"
        @$state.go("databaseDocumentEdit", {documentId: documentId})

    goToXRay: (documentId) ->
        @$log.debug "DatabaseCtrl.goToXRay(#{documentId})"
        @$state.go("databaseDocumentXRay", {documentId: documentId})

    goToShare: (documentId) ->
        @$log.debug "DatabaseCtrl.goToShare(#{documentId})"
        @$state.go("databaseDocumentShare", {documentId: documentId})
    
    goToView: (documentId) ->
        @$log.debug "DatabaseCtrl.goToView(#{documentId})"
        @$state.go("databaseDocumentView", {documentId: documentId})

    goToUpload: () ->
        @$log.debug "DatabaseCtrl.goToUpload()"
        @$state.go("databaseUpload")

    goToSearch: () ->
        @$log.debug "DatabaseCtrl.goToSearch()"
        @$state.go("databaseSearch")

    showRemoveAlert: (documentId) ->
        @$log.debug "DatabaseCtrl.showRemoveAlert(#{documentId})"
        @removeId = documentId
        @removeAlert = true
    
    copyDocument: (documentId) ->
        @$log.debug "DatabaseCtrl.copyDocument(#{documentId})"
        @DatabaseService.copyDocument(documentId).then(
            (data) =>
                # show status
                @$log.debug "Successfully copied document"
                @listDocuments()
            ,
            (error) =>
                @$log.error "Unable to copy document: #{error}"
                @ErrorService.error
            )

    getUrl: (documentId) ->
        @$log.debug "DatabaseCtrl.getUrl(#{documentId})"
        "/database/download/#{documentId}"

    cancelRemove: () ->
        @$log.debug "DatabaseCtrl.cancelRemove()"
        @removeAlert = false
        @removeId

    remove: () ->
        @$log.debug "DatabaseCtrl.remove()"
        @removeAlert = false
        @Document.remove({documentId: @removeId}).$promise.then(
            (data) =>
                @$log.debug "Successfully deleted document!"
                @listDocuments()
            ,
            (error) =>
                @$log.error "Unable to delete document: #{error}!"
            )

    search: (searchText) ->
        @$log.debug "DatabaseCtrl.search(#{searchText})"
        if (searchText)
          @documents = []
          @DatabaseService.search(searchText).then(
            (data) =>
                @$log.debug "Successfully returned search result #{data.length}"
                @documents = data
            ,
            (error) =>
                @$log.error "Unable to search #{searchText}"
            )
        else
          @listDocuments()


controllersModule.controller('DatabaseCtrl', DatabaseCtrl)