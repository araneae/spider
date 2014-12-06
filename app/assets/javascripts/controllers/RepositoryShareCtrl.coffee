
class RepositoryShareCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q, @Document, 
                              @DatabaseService, @UtilityService, @$location, @ErrorService) ->
        @$log.debug "constructing RepositoryShareCtrl"
        today = new Date()
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

        @isDatePickerOpened = false
        # load objects from server
        @loadShareContacts()

    loadShareContacts: () ->
        @$log.debug "RepositoryShareCtrl.loadShareContacts()"
        @DatabaseService.getRepositoryShareContacts().then(
            (data) => 
              @$log.debug "Promise returned #{data} contacts"
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
   
    formatDate: (date) ->
      dateString = null
      if (date)
        year = date.getFullYear()
        month = date.getMonth() + 1
        day = date.getDate()
        dateString = year + "-" + month + "-" + day
      dateString

    cancel: () ->
      @$log.debug "RepositoryShareCtrl.cancel()"
      @$state.go("database.documents")
   
    update: () ->
      @$log.debug "RepositoryShareCtrl.update()"
      dirtyObjs = []
      for obj in @sharedContacts
        orgObj = @sharedContactsOrg[obj.id]
        equals = angular.equals(obj, orgObj)
        if (!equals)
            obj.shareUntilEOD = @formatDate(obj.shareUntilEOD)
            dirtyObjs.push(obj)
            
      @DatabaseService.shareRepository(dirtyObjs).then(
          (data) => 
            @$log.debug "Promise returned #{data} contacts"
            @ErrorService.success("Successfully updated sharing repository.")
            @loadShareContacts()
          ,
          (error) =>
            @$log.error "Unable to share contacts: #{error}"
            @ErrorService.error("Unable to share repository!")
      )

controllersModule.controller('RepositoryShareCtrl', RepositoryShareCtrl)