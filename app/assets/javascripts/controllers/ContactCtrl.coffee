
class ContactCtrl

    constructor: (@$log, @ContactService, @Contact, @$location) ->
        @$log.debug "constructing ContactCtrl"
        @contacts = []
        @searchText
        @searchResult = {}
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
                @$log.error "Unable to get my contacts: #{error}"
            )

    search: () ->
      @$log.debug "ContactCtrl.search()"
      
      @ContactService.search(@searchText).then(
         (data) =>
                @$log.debug "Successfully returned search result #{data}"
                @searchResult = data
         ,
         (error) =>
            @$log.error "Unable to search #{@searchText}"
      )

    invite: () ->
      @$log.debug "ContactCtrl.invite()"
      @Contact.save({contactUserId: @searchResult.id}).$promise.then(
          (data) =>
            @$log.debug "Successfully invited #{data}"
            @listContacts()
         ,
         (error) =>
            @$log.error "Unable to invite #{@searchText}"
      )

    showSearchResult: () ->
        @searchResult.id

controllersModule.controller('ContactCtrl', ContactCtrl)