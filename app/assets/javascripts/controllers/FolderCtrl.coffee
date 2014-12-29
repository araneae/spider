
class FolderCtrl

    constructor: (@$log, @$q, @$scope, @$state, @$stateParams, @FolderService,
                                      @UtilityService, @DocumentFolder, @DocumentTag, @ErrorService) ->
        @$log.debug "constructing FolderCtrl"
        @folders = []
        @foldersMgm = []
        @removedIds = []
        
        # fetch data from server
        @listFolders()

    listFolders: () ->
        @$log.debug "FolderCtrl.listFolders()"
        @DocumentFolder.query().$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data.length} folders"
              @folders = data
              #@folders.unshift({documentFolderId: 0, name: 'All'})
          ,
          (error) =>
              @$log.error "Unable to fetch folders: #{error}"
              @ErrorService.error("Unable to fetch folders from server!")
          )

    isRenameDisabled: (folder) ->
      @$log.debug "FolderCtrl.isRenameDisabled(#{folder})"
      folder.documentFolderId is 0 or folder.shared
    
    isHideDelete: (folder) ->
      @$log.debug "FolderCtrl.isHideDelete(#{folder})"
      folder.documentFolderId is 0 or folder.default or folder.shared

    goToDocumentFolder: (folder) ->
        @$log.debug "FolderCtrl.goToDocumentFolder(#{folder})"
        @$state.go("folder.documents", {documentFolderId: folder.documentFolderId})

    goToDocumentFolderManagement: () ->
        @$log.debug "FolderCtrl.goToDocumentFolderManagement()"
        @foldersMgm = angular.copy(@folders)
        @$state.go("folder.documentFolderManagement")

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

    remove: (folder) ->
        @$log.debug "FolderCtrl.remove(#{folder})"
        @removedIds.push(folderId)
        index = @UtilityService.findIndexByProperty(@foldersMgm, 'documentFolderId', folder.documentFolderId)
        @foldersMgm.splice(index, 1)

    save: () ->
        @$log.debug "FolderCtrl.save()"
        promises = []
        for documentFolderId in @removedIds
            promise = @DocumentFolder.remove({documentFolderId: documentFolderId}).$promise
            promises.push(promise)
        
        for folder in @foldersMgm
            orgObj = @UtilityService.findByProperty(@folders, 'documentFolderId', folder.documentFolderId)
            equals = angular.equals(folder, orgObj)
            if (!equals and @UtilityService.isStringEmpty(folder.name))
              promise = @DocumentFolder.update(folder).$promise
              promises.push(promise)

        # wait for all the promises to complete
        if (promises.length > 0)
          @$q.all(promises).then(
            (data) =>
                 @$log.debug "Successfully updated all the folders!"
                 @ErrorService.success("Successfully updated folders!")
                 @listFolders()
                 @$state.go('folder.documents')
            ,
            (error) =>
                @$log.debug "one of the promise failed"
                @listFolders()
                @ErrorService.error("Unable to update folder #{angular.toJson(error)}!")
          )

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