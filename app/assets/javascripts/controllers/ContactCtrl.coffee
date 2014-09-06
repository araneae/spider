
class ContactCtrl

    constructor: (@$log, @$scope, @ContactService, @Contact, @$location, @UtilityService) ->
        @$log.debug "constructing ContactCtrl"
        @contacts = []
        @searchMode = false
        @$scope.$on('globalSearch', (event, data) =>
                                    @$log.debug "received message globalSearch(#{data.searchText})"
                                    @search(data.searchText)
        )
        # fetch data from server
        @listContacts()

    listContacts: () ->
        @$log.debug "ContactCtrl.listContacts()"
        @searchMode = false
        @Contact.query().$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} my contacts"
                @contacts = data
            ,
            (error) =>
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
              @searchMode = true
              @contacts = data
          ,
          (error) =>
              @$log.error "Unable to search #{searchText}"
        )

    invite: (userId) ->
      @$log.debug "ContactCtrl.invite(#{userId})"
      @Contact.save({contactUserId: userId}).$promise.then(
          (data) =>
            @$log.debug "Successfully invited #{data}"
            @listContacts()
         ,
         (error) =>
            @$log.error "Unable to invite #{userId}"
      )

controllersModule.controller('ContactCtrl', ContactCtrl)