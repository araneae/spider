
class FolderDocumentCtrl

    constructor: (@$log, @$scope, @FolderService, @$state, @$stateParams, @Document, @DocumentFolder, @$previousState,
                                            @DatabaseService, @UtilityService, @ErrorService, @ConfigService) ->
        @$log.debug "constructing FolderDocumentCtrl"
        @documentFolderId = 0
        @documentFolderId = @UtilityService.parseInteger(@$stateParams.documentFolderId)
        @documents = []
        @folder = {}
        @title ="All Documents"
        @displayCollection = []
        @removeId
        @removeAlert = false
        @searchText
        
        @$scope.$on('contextMenu', (event, data) =>
                                    @$log.debug "received message contextMenu(#{data.menuItem})"
                                    @refresh() if data.menuItem is "refresh"
        )
        
        @$previousState.memo("FolderDocumentCtrl")
        
        # load list of documents from server
        @loadFolder(@documentFolderId) if (@documentFolderId > 0)
        @listDocuments()

    loadFolder: (documentFolderId) ->
        @$log.debug "FolderDocumentCtrl.loadFolder(#{documentFolderId})"
        @DocumentFolder.get({documentFolderId: documentFolderId}).$promise.then(
          (data) =>
              @$log.debug "Promise returned #{angular.toJson(data)} folder"
              @folder = data
              @title = "Documents in #{data.name}"
          ,
          (error) =>
              @$log.error "Unable to fetch folder: #{error}"
              @ErrorService.error("Unable to fetch folder from server!")
          )
          
    listDocuments: () ->
        @$log.debug "FolderDocumentCtrl.listDocuments()"
        if (@documentFolderId)
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
      @loadFolder(@documentFolderId)
      @listDocuments()

    showEditLink: (document) ->
        (!@folder.shared)
    
    showViewLink: (document) ->
        (document.canView and @folder.canView)
    
    showXRayLink: (document) ->
        (document.canView and @folder.canView)

    showShareLink: (document) ->
        (document.canShare and !@folder.shared)
    
    showCopyLink: (document) ->
        (@folder.canCopy)
    
    showDeleteLink: () ->
        (!@folder.shared)
        
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
        @$state.go("databaseManageShares", {documentId: documentId})
    
    goToView: (documentId) ->
        @$log.debug "FolderDocumentCtrl.goToView(#{documentId})"
        @$state.go("databaseDocumentView", {documentFolderId: @documentFolderId, documentId: documentId})

    goToShareFolder: () ->
        @$log.debug "FolderDocumentCtrl.goToShareFolder()"
        @$state.go("folder.shareFolder")

    goToUpload: () ->
        @$log.debug "FolderDocumentCtrl.goToUpload()"
        @$state.go("folder.upload")

    isAllowDragDrop: (document) ->
        @$log.debug "FolderDocumentCtrl.isAllowDragDrp(#{document})"
        document.ownershipType is 'OWNED' 

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
                @ErrorService.error(error.data.message)
                @$log.error "Unable to delete document: #{error}!"
            )

    clearSearchText: () ->
        @searchText = ""
        @search()
    
    search: () ->
        @$log.debug "FolderDocumentCtrl.search()"
        if (@searchText)
          @documents = []
          @FolderService.searchInFolder(@documentFolderId, @searchText).then(
            (data) =>
                @$log.debug "Successfully returned search result #{data.length}"
                @documents = data
            ,
            (error) =>
                @ErrorService.error("Oops! Unable to search.")
                @$log.error "Unable to search #{@searchText}"
            )
        else
          @listDocuments()

controllersModule.controller('FolderDocumentCtrl', FolderDocumentCtrl)