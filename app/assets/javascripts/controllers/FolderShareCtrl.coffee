
class FolderShareCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q, @Document, @DocumentFolder,
                              @DatabaseService, @FolderService, @UtilityService, @$location, @ErrorService) ->
        @$log.debug "constructing FolderShareCtrl"
        @documentFolderId = 0
        @documentFolderId = parseInt(@$stateParams.documentFolderId) if @UtilityService.isNumber(@$stateParams.documentFolderId)
        today = new Date()
        @sharedContacts = []
        @sharedContactsOrg = {}
        @datePickerOpenedFlag = {}
        @dateOptions = {
           startingDay : 1,
           format : 'MM/dd/yyyy',
           minDate : today
        }
        @folder = {}
        @title = {}
        # method to be used in "watch" method
        @$scope.isLimitedShare = () =>  
            @share.isLimitedShare

        @isDatePickerOpened = false
        
        @$scope.$on('contextMenu', (event, data) =>
                                    @$log.debug "received message contextMenu(#{data.menuItem})"
                                    @refresh() if data.menuItem is "refresh"
        )
        
        # load objects from server
        @loadFolder(@documentFolderId) if (@documentFolderId > 0)
        @loadShareContacts()
    
    refresh: () ->
        @loadFolder(@documentFolderId) if (@documentFolderId > 0)
        @loadShareContacts()

    loadFolder: (documentFolderId) ->
        @$log.debug "FolderShareCtrl.loadFolder(#{documentFolderId})"
        @DocumentFolder.get({documentFolderId: documentFolderId}).$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data} folder"
              @folder = data
              @title = "Sharing folder #{data.name}"
          ,
          (error) =>
              @$log.error "Unable to fetch folder: #{error}"
              @ErrorService.error("Unable to fetch folder from server!")
          )

    loadShareContacts: () ->
        @$log.debug "FolderShareCtrl.loadShareContacts()"
        @FolderService.getFolderShareContacts(@documentFolderId).then(
            (data) => 
              @$log.debug "Promise returned #{data} contacts"
              @sharedContacts = []
              for obj in data
                @sharedContacts.push(obj)
                @sharedContactsOrg[obj.id] = angular.copy(obj)
                @datePickerOpenedFlag[obj.id] = false
            ,
            (error) =>
              @$log.error "Unable to get contacts: #{error}"
              @ErrorService.error
        )

    disableUpdate: () ->
      @UtilityService.isArrayEmpty(@sharedContacts)

    openDatePicker: (event, contact) ->
      event.preventDefault()
      event.stopPropagation()
      @datePickerOpenedFlag[contact.id] = true
   
    cancel: () ->
      @$log.debug "FolderShareCtrl.cancel()"
      @$state.go("folder.documents")
   
    update: () ->
      @$log.debug "FolderShareCtrl.update()"
      dirtyObjs = []
      for obj in @sharedContacts
        orgObj = @sharedContactsOrg[obj.id]
        equals = angular.equals(obj, orgObj)
        if (!equals)
            obj.shareUntilEOD = @UtilityService.formatDate(obj.shareUntilEOD)
            dirtyObjs.push(obj)

      if (dirtyObjs.length > 0)
        @FolderService.shareFolder(@documentFolderId, dirtyObjs).then(
          (data) => 
            @$log.debug "Promise returned #{data} contacts"
            @ErrorService.success("Successfully updated shares.")
            @loadShareContacts()
          ,
          (error) =>
            @$log.error "Unable to share contacts: #{error}"
            @ErrorService.error("Unable to share folder!")
        )

controllersModule.controller('FolderShareCtrl', FolderShareCtrl)