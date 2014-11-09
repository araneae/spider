
class ContactInviteCtrl

    constructor: (@$log, @$scope,  @$state, @$stateParams, @ContactService, @ErrorService, @$location) ->
        @$log.debug "constructing ContactInviteCtrl"
        @contactId = parseInt(@$stateParams.contactId)
        @contactInvite = {}
        # fetch data from server
        @loadInviteMessage()

    loadInviteMessage: () ->
        @$log.debug "ContactInviteCtrl.loadContact()"
        @ContactService.getInviteMessage(@contactId).then(
            (data) =>
                @$log.debug "Promise returned #{data}"
                @contactInvite = data
            ,
            (error) =>
                @ErrorService.error("Oops, something wrong! Unable to fetch data from server.")
                @$log.error "Unable to fetch data : #{error}"
            )

    cancel: () ->
      @$state.go("contact")
    
    send: () ->
      @$log.debug "ContactInviteCtrl.send(#{@contactId})"
      @ContactService.sendInvite(@contactId, @contactInvite).then(
          (data) =>
            @ErrorService.success("Successfully sent invitation.")
            @$log.debug "Successfully invited #{data}"
            @$state.go("contact")
         ,
         (error) =>
            @ErrorService.error("Oops, something wrong! Unable to send invitation.")
            @$log.error "Unable to send invitation"
      )

controllersModule.controller('ContactInviteCtrl', ContactInviteCtrl)