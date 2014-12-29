
class FolderDocumentCtrl

    constructor: (@$log, @$scope, @FolderService, @$state, @$stateParams, @Document, @DocumentFolder, @$previousState,
                                            @DatabaseService, @UtilityService, @ErrorService, @ConfigService) ->
        @$log.debug "constructing FolderDocumentCtrl"
        @documentFolderId = 0
        @documentFolderId = parseInt(@$stateParams.documentFolderId) if @UtilityService.isNumber(@$stateParams.documentFolderId)
        @documents = []
        @folder = {}
        @title ="All Documents"
        @displayCollection = []
        @removeId
        @removeAlert = false
        @$scope.$on('globalSearch', (event, data) =>
                                    @$log.debug "received message globalSearch(#{data.searchText})"
                                    @search(@documentFolderId, data.searchText)
        )
        @$scope.$on('contextMenu', (event, data) =>
                                    @$log.debug "received message contextMenu(#{data.menuItem})"
                                    @refresh() if data.menuItem is "refresh"
                                    @goToUpload() if data.menuItem is "uploadFile"
                                    @goToSearch() if data.menuItem is "advancedSearch"
                                    @goToShareFolder() if data.menuItem is "shareFolder"
        )
        
        @$previousState.memo("FolderDocumentCtrl")
        
        # load list of documents from server
        @loadFolder(@documentFolderId) if (@documentFolderId > 0)
        @listDocuments()

    loadFolder: (documentFolderId) ->
        @$log.debug "FolderDocumentCtrl.loadFolder(#{documentFolderId})"
        @DocumentFolder.get({documentFolderId: documentFolderId}).$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data} folder"
              @folder = data
              @title = "Documents in #{data.name}"
          ,
          (error) =>
              @$log.error "Unable to fetch folder: #{error}"
              @ErrorService.error("Unable to fetch folder from server!")
          )
          
    listDocuments: () ->
        @$log.debug "FolderDocumentCtrl.listDocuments()"
        @DatabaseService.getDocumentByDocumentFolderId(@documentFolderId).then(
            (data) =>
                @$log.debug "Promise returned #{data.length} documents"
                @documents = data
                @displayCollection = angular.copy(data)
                # update scope to refresh ui
                #@$scope.$apply()
            ,
            (error) =>
                @$log.error "Unable to get documents: #{error}"
            )
        
    refresh: () ->
      @listDocuments()
    
    goToDocumentTag: (documentId) ->
        @$log.debug "FolderDocumentCtrl.goToDocumentTag(#{documentId})"
        @$state.go("databaseDocumentTag", {documentId: documentId})

    goToDocumentEdit: (documentId) ->
        @$log.debug "FolderDocumentCtrl.goToDocumentEdit(#{documentId})"
        @$state.go("databaseDocumentEdit", {documentId: documentId})

    goToXRay: (documentId) ->
        @$log.debug "FolderDocumentCtrl.goToXRay(#{documentId})"
        @$state.go("databaseDocumentXRay", {documentId: documentId})

    goToShare: (documentId) ->
        @$log.debug "FolderDocumentCtrl.goToShare(#{documentId})"
        @$state.go("databaseDocumentShare", {documentId: documentId})
    
    goToView: (documentId) ->
        @$log.debug "FolderDocumentCtrl.goToView(#{documentId})"
        @$state.go("databaseDocumentView", {documentId: documentId})

    goToUpload: () ->
        @$log.debug "FolderDocumentCtrl.goToUpload()"
        @$state.go("databaseUpload")

    goToSearch: () ->
        @$log.debug "FolderDocumentCtrl.goToSearch()"
        @$state.go("databaseSearch")
  
    goToShareFolder: () ->
        @$log.debug "FolderDocumentCtrl.goToShareFolder()"
        if (@documentFolderId > 0)
          @$state.go("folder.shareFolder")
        else
          @ErrorService.error("Select a folder to share!")

    showRemoveAlert: (documentId) ->
        @$log.debug "FolderDocumentCtrl.showRemoveAlert(#{documentId})"
        @removeId = documentId
        @removeAlert = true
    
    copyDocument: (documentId) ->
        @$log.debug "FolderDocumentCtrl.copyDocument(#{documentId})"
        @DocumentService.copyDocument(documentId).then(
            (data) =>
                # show status
                @$log.debug "Successfully copied document"
                @listDocuments()
            ,
            (error) =>
                @$log.error "Unable to copy document: #{error}"
                @ErrorService.error
            )

    getDownloadUrl: (documentId) ->
        #@$log.debug "FolderDocumentCtrl.getUrl(#{documentId})"
        "/database/download/#{documentId}"

    cancelRemove: () ->
        @$log.debug "FolderDocumentCtrl.cancelRemove()"
        @removeAlert = false
        @removeId

    remove: () ->
        @$log.debug "FolderDocumentCtrl.remove()"
        @removeAlert = false
        @Document.remove({documentId: @removeId}).$promise.then(
            (data) =>
                @$log.debug "Successfully deleted document!"
                @listDocuments()
            ,
            (error) =>
                @ErrorService.error("Oops! Unable to delete document.")
                @$log.error "Unable to delete document: #{error}!"
            )

    search: (documentFolderId, searchText) ->
        @$log.debug "FolderDocumentCtrl.search(#{documentFolderId}, #{searchText})"
        if (searchText)
          @documents = []
          @FolderService.search(documentFolderId, searchText).then(
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

controllersModule.controller('FolderDocumentCtrl', FolderDocumentCtrl)