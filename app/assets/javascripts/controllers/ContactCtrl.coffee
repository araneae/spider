
class ContactCtrl

    constructor: (@$log, @$scope,  @$state, @ContactService, @ErrorService, @Contact, @$location, @UtilityService) ->
        @$log.debug "constructing ContactCtrl"
        @contacts = []
        @$scope.$on('globalSearch', (event, data) =>
                                    @$log.debug "received message globalSearch(#{data.searchText})"
                                    @search(data.searchText)
        )
        # fetch data from server
        @listContacts()

    listContacts: () ->
        @$log.debug "ContactCtrl.listContacts()"
        @Contact.query().$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} my contacts"
                @contacts = data
            ,
            (error) =>
                @ErrorService.error("Oops, something wrong! Unable to fetch data from server.")
                @$log.error "Unable to get my contacts: #{error}"
            )

    search: (searchText) ->
      @$log.debug "ContactCtrl.search(#{searchText})"
      if @UtilityService.isEmpty(searchText)
          @listContacts()
      else 
        @ContactService.search(searchText).then(
          (data) =>
              @$log.debug "Successfully returned search result #{data}"
              @contacts = data
          ,
          (error) =>
              @ErrorService.error("Oops, something wrong! Unable to search #{searchText}.")
              @$log.error "Unable to search #{searchText}"
        )
    
    getStatusDisplayName: (status) ->
      return "Not Connected" if status is 'NOTCONNECTED'
      return "Connected" if status is 'CONNECTED'
      return "Rejected" if status is 'REJECTED'
      return "Pending" if status is 'PENDING'
      return "Awaiting" if status is 'AWAITING'
      ""
    
    getInviteTitle: (status) ->
      return "Send Invite" if status is 'NOTCONNECTED'
      "Resend Invite"
      return "Send Invite" if status is 'NOTCONNECTED'
      return "Resend Invite" if status is 'REJECTED'
      return "Resend Invite" if status is 'PENDING'
      ""
    
    showSendInvite: (contact) ->
      contact.status is 'NOTCONNECTED' or 
      contact.status is 'PENDING' or 
      contact.status is 'REJECTED'
    
    showAccept: (contact) ->
      contact.status is 'AWAITING'
      
    showReject: (contact) ->
      contact.status is 'AWAITING'

    accept: (contact) ->
      @$log.debug "ContactCtrl.accept(#{contact.contactId})"
      @ContactService.accept(contact.contactId).then(
          (data) =>
            @$log.debug "Successfully accepted invitation #{data}"
            @listContacts()
         ,
         (error) =>
            @ErrorService.error("Oops, something wrong! Unable to accept invitation.")
            @$log.error "Unable to accept invitation"
      )
      
    reject: (contact) ->
      @$log.debug "ContactCtrl.reject(#{contact.contactId})"
      @ContactService.reject(contact.contactId).then(
          (data) =>
            @$log.debug "Successfully rejected invitation #{data}"
            @listContacts()
         ,
         (error) =>
            @ErrorService.error("Oops, something wrong! Unable to reject invitation.")
            @$log.error "Unable to reject invitation"
      )
      
    goToInvite: (contact) ->
      @$log.debug "ContactCtrl.goToInvite(#{contact.contactId})"
      @$state.go("contactInvite", {contactId: contact.contactId})

controllersModule.controller('ContactCtrl', ContactCtrl)