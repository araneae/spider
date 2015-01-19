
class DatabaseManageShareCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q, @Document, 
                              @DatabaseService, @UtilityService, @$location, @ErrorService) ->
        @$log.debug "constructing DatabaseManageShareCtrl"
        @documentId = parseInt(@$stateParams.documentId)
        @document = {}
        @share = {}
        today = new Date()
        @contacts = []
        @sharedContacts = []
        @sharedContactsOrg = {}
        @datePickerOpenedFlag = {}
        @dateOptions = {
           startingDay : 1,
           format : 'MM/dd/yyyy',
           minDate : today,
        }
        
        # method to be used in "watch" method
        @$scope.isLimitedShare = () =>  
            @share.isLimitedShare

        # watching for any change in "Limited Share"
        @$scope.$watch('isLimitedShare()', (newVal) =>
                       @share.canShare = false if newVal
                      )
        @isDatePickerOpened = false
        # load objects from server
        @loadDocument(@documentId)
        @loadShareContacts(@documentId)

    loadDocument: (documentId) ->
        @$log.debug "DatabaseManageShareCtrl.loadDocument(#{documentId})"
        #delay = @$q.defer()
        @Document.get({documentId: documentId}).$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data} document"
              @document = data
              @share.subject = "Shared a document \"#{@document.name}\""
          ,
          (error) =>
              @$log.error "Unable to get document: #{error}"
              @ErrorService.error("Unable to fetch data from server!")
          )

    loadShareContacts: (documentId) ->
        @$log.debug "DatabaseManageShareCtrl.loadShareContacts(#{documentId})"
        @DatabaseService.getShareContacts(documentId).then(
            (data) => 
              @$log.debug "Promise returned #{data} contacts"
              for obj in data
                if (obj.documentId)
                  @sharedContacts.push(obj)
                  @sharedContactsOrg[obj.id] = angular.copy(obj)
                  @datePickerOpenedFlag[obj.id] = false
            ,
            (error) =>
              @$log.error "Unable to get contacts: #{error}"
              @ErrorService.error("Unable to fetch contacts from server!")
        )

    disableUpdate: () ->
      @UtilityService.isArrayEmpty(@sharedContacts)

    goToAddShare: () ->
      @$log.debug "DatabaseManageShareCtrl.goToAddShare()"
      @$state.go("databaseDocumentShare", {documentId: @documentId})

    openDatePicker: (event, contact) ->
      event.preventDefault()
      event.stopPropagation()
      @datePickerOpenedFlag[contact.id] = true
   
    cancel: () ->
      @$log.debug "DatabaseManageShareCtrl.cancel()"
      @$state.go("folder.documents")
   
    update: () ->
      @$log.debug "DatabaseManageShareCtrl.update()"
      for obj in @sharedContacts
        orgObj = @sharedContactsOrg[obj.id]
        equals = angular.equals(obj, orgObj)
        if (!equals)
            obj.shareUntilEOD = @UtilityService.formatDate(new Date(obj.shareUntilEOD))
            @DatabaseService.updateShare(@documentId, obj).then(
                (data) => 
                  @$log.debug "Promise returned #{data} contacts"
                  @ErrorService.success("Successfully updated shares!")
                  @$state.go('database.documents')
                ,
                (error) =>
                  @$log.error "Unable to share contacts: #{error}"
                  @ErrorService.error("Unable to update shares!")
            )

controllersModule.controller('DatabaseManageShareCtrl', DatabaseManageShareCtrl)