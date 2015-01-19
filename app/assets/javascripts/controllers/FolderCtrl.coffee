
class FolderCtrl

    constructor: (@$log, @$q, @$scope, @$state, @$stateParams, @FolderService,
                                      @UtilityService, @DocumentFolder, @DocumentTag, @ErrorService) ->
        @$log.debug "constructing FolderCtrl"
        @folders = []
        @documentFolderId = @UtilityService.parseInteger(@$stateParams.documentFolderId)
        
        @$scope.$on('contextMenu', (event, data) =>
                                    @$log.debug "received message contextMenu(#{data.menuItem})"
                                    @refresh() if data.menuItem is "refresh"
                                    @goToUpload() if data.menuItem is "uploadFile"
                                    @goToSearch() if data.menuItem is "advancedSearch"
                                    @goToShareFolder() if data.menuItem is "shareFolder"
        )
        
        # fetch data from server
        @listFolders()

    listFolders: () ->
        @$log.debug "FolderCtrl.listFolders()"
        @DocumentFolder.query().$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data.length} folders"
              @folders = data
              #@folders.unshift({documentFolderId: 0, name: 'All'})
              if (data and data.length > 0)
                @goToDocumentFolder(data[0]) if !@documentFolderId
          ,
          (error) =>
              @$log.error "Unable to fetch folders: #{error}"
              @ErrorService.error("Unable to fetch folders from server!")
          )

    refresh: () ->
      @listFolders()

    getFolderIcon: (folder) ->
      if (folder and (folder.documentFolderId is @documentFolderId))
          '/assets/images/icon-folder-open.png'
      else 
          '/assets/images/icon-folder.png'

    goToUpload: () ->
        @$log.debug "FolderCtrl.goToUpload()"
        @$state.go("databaseUpload")

    goToSearch: () ->
        @$log.debug "FolderCtrl.goToSearch()"
        @$state.go("databaseSearch")
  
    goToShareFolder: () ->
        @$log.debug "FolderCtrl.goToShareFolder()"
        if (@documentFolderId)
          @$state.go("folder.shareFolder")
        else
          @ErrorService.error("Select a folder to share!")

    goToDocumentFolder: (folder) ->
        @$log.debug "FolderCtrl.goToDocumentFolder(#{folder})"
        @$state.go("folder.documents", {documentFolderId: folder.documentFolderId})

    goToDocumentFolderManagement: () ->
        @$log.debug "FolderCtrl.goToDocumentFolderManagement()"
        @$state.go("documentFolderManagement")

    goToDocumentFolderCreate: () ->
        @$log.debug "FolderCtrl.goToDocumentFolderCreate()"
        @$state.go("folder.folderCreate")
   
    goToUserTags: () ->
        @$log.debug "FolderCtrl.goToUserTags()"
        @$state.go("database.documents")

    isAllowDragDrop: (folder) ->
        @$log.debug "FolderCtrl.isAllowDragDrp(#{folder})"
        !folder.shared

    showDocumentFolderManagement: () ->
        @$log.debug "FolderCtrl.showDocumentFolderManagement()"
        @folders.length > 0

    cancel: () ->
        @$log.debug "FolderCtrl.cancel()"
        @$state.go("folder.documents")

    onDropComplete: (document, evt, folder) ->
        @$log.debug "FolderCtrl.onDropComplete(#{document}, #{folder})"
        if (folder.documentFolderId > 0)
          @FolderService.moveToFolder(document.documentId, folder.documentFolderId).then(
              (data) =>
                  @ErrorService.success("Successfully moved document #{document.name} to folder #{folder.name}.")
                  @$log.debug "Successfully moved document to folder #{data}"
              ,
              (error) =>
                  @ErrorService.error(error.data.message)
                  @$log.debug("Unable to move document to folder #{error}.")
            )

controllersModule.controller('FolderCtrl', FolderCtrl)