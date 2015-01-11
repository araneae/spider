
class SharedDocumentShareCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q, @Document,
                              @DatabaseService, @UtilityService, @$location, @ErrorService) ->
        @$log.debug "constructing SharedDocumentShareCtrl"
        @documentId = parseInt(@$stateParams.documentId)
        @document = {}
        today = new Date()
        @share = {
           subject: "",
           message: "",
           canCopy: true,
           canShare: true,
           canView: true,
           isLimitedShare: false,
           shareUntilEOD: null,
           receivers: []
        }
        @contacts = []
        @select2Options = {
           data : @contacts,
           multiple : true
        }
        @dateOptions = {
           startingDay : 1,
           format : 'MM/dd/yyyy',
           minDate : today,
        }
        
        # for watching property change
        @$scope.isLimitedShare = () =>  
            @share.isLimitedShare

        # watching for any change in "Limited Share" control
        @$scope.$watch('isLimitedShare()', (newVal) =>
                       @share.canShare = false if newVal
                      )
        @isDatePickerOpened = false
        
        # load objects from server
        @loadDocument(@documentId)
        @loadShareContacts(@documentId)

    loadDocument: (documentId) ->
        @$log.debug "SharedDocumentShareCtrl.loadDocument(#{documentId})"
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
        @$log.debug "SharedDocumentShareCtrl.loadShareContacts(#{documentId})"
        @DatabaseService.getShareContacts(documentId).then(
            (data) => 
              @$log.debug "Promise returned #{data} contacts"
              for obj in data
                @contacts.push({id: obj.id, text: obj.text}) if !obj.documentId
            ,
            (error) =>
              @$log.error "Unable to get contacts: #{error}"
              @ErrorService.error("Unable to fetch contacts from server!")
        )
    
    disableShare: () ->
      @UtilityService.isArrayEmpty(@share.receivers)

    openDatePicker: (event) ->
      event.preventDefault()
      event.stopPropagation()
      @isDatePickerOpened = true
  
    sendShare: () ->
      @$log.debug "SharedDocumentShareCtrl.sendShare()"
      @share.shareUntilEOD = @UtilityService.formatDate(@share.shareUntilEOD)
      @DatabaseService.share(@documentId, @share).then(
            (data) => 
              @$log.debug "Promise returned #{data} contacts"
              @ErrorService.success("Successfully shared document!")
              @$state.go('sharedRepositories')
            ,
            (error) =>
              @$log.error "Unable to share contacts: #{error}"
              @ErrorService.error("Unable to share document!")
      )
    
    goToManageShares: () ->
      @$log.debug "SharedDocumentShareCtrl.goToManageShares()"
      @$state.go("sharedDocumentManageShares", {documentId: @documentId})
    
    cancel: () ->
      @$log.debug "SharedDocumentShareCtrl.cancel()"
      @$state.go('sharedRepositories')

controllersModule.controller('SharedDocumentShareCtrl', SharedDocumentShareCtrl)