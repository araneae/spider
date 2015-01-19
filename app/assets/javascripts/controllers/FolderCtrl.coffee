
class FolderCtrl

    constructor: (@$log, @$q, @$scope, @$state, @$stateParams, @FolderService,
                                      @UtilityService, @DocumentFolder, @DocumentTag, @ErrorService) ->
        @$log.debug "constructing FolderCtrl"
        @folders = []
        
        # fetch data from server
        @listFolders()

    listFolders: () ->
        @$log.debug "FolderCtrl.listFolders()"
        @DocumentFolder.query().$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data.length} folders"
              @folders = data
              #@folders.unshift({documentFolderId: 0, name: 'All'})
              if (data && data.length > 0)
                @goToDocumentFolder(data[0])
          ,
          (error) =>
              @$log.error "Unable to fetch folders: #{error}"
              @ErrorService.error("Unable to fetch folders from server!")
          )

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
        @$log.debug "FolderCtrl.isAllowDragDrp(#{angular.toJson(folder)})"
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