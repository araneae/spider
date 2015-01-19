
class ManageFolderCtrl

    constructor: (@$log, @$q, @$scope, @$state, @$stateParams, @FolderService,
                                      @UtilityService, @DocumentFolder, @DocumentTag, @ErrorService) ->
        @$log.debug "constructing ManageFolderCtrl"
        @folders = []
        @foldersMgm = []
        @removedIds = []
        
        # fetch data from server
        @listFolders()

    listFolders: () ->
        @$log.debug "ManageFolderCtrl.listFolders()"
        @DocumentFolder.query().$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data.length} folders"
              @folders = data
              @foldersMgm = angular.copy(@folders)
          ,
          (error) =>
              @$log.error "Unable to fetch folders: #{error}"
              @ErrorService.error("Unable to fetch folders from server!")
          )

    isRenameDisabled: (folder) ->
      @$log.debug "ManageFolderCtrl.isRenameDisabled(#{folder})"
      folder.documentFolderId is 0 or folder.shared
    
    isHideDelete: (folder) ->
      @$log.debug "ManageFolderCtrl.isHideDelete(#{folder})"
      folder.documentFolderId is 0 or folder.default or folder.shared

    cancel: () ->
        @$log.debug "ManageFolderCtrl.cancel()"
        @$state.go("folder.documents")

    remove: (folder) ->
        @$log.debug "ManageFolderCtrl.remove(#{folder})"
        @removedIds.push(folderId)
        index = @UtilityService.findIndexByProperty(@foldersMgm, 'documentFolderId', folder.documentFolderId)
        @foldersMgm.splice(index, 1)

    save: () ->
        @$log.debug "ManageFolderCtrl.save()"
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

controllersModule.controller('ManageFolderCtrl', ManageFolderCtrl)