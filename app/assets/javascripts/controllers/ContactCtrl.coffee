
class ContactCtrl

    constructor: (@$log, @ContactService, @Contact, @$location, @UtilityService) ->
        @$log.debug "constructing ContactCtrl"
        @contacts = []
        @searchText
        @searchMode = false
        # fetch data from server
        @listContacts()

    listContacts: () ->
        @$log.debug "ContactCtrl.listContacts()"
        @searchMode = false
        @searchText
        @Contact.query().$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} my contacts"
                @contacts = data
            ,
            (error) =>
                @$log.error "Unable to get my contacts: #{error}"
            )

    search: () ->
      @$log.debug "ContactCtrl.search()"
      if @UtilityService.isEmpty(@searchText)
          @listContacts()
      else 
        @ContactService.search(@searchText).then(
          (data) =>
              @$log.debug "Successfully returned search result #{data}"
              @searchMode = true
              @contacts = data
          ,
          (error) =>
              @$log.error "Unable to search #{@searchText}"
        )

    invite: (id) ->
      @$log.debug "ContactCtrl.invite()"
      @Contact.save({contactUserId: id}).$promise.then(
          (data) =>
            @$log.debug "Successfully invited #{data}"
            @listContacts()
         ,
         (error) =>
            @$log.error "Unable to invite #{@searchText}"
      )

controllersModule.controller('ContactCtrl', ContactCtrl)