
class MessageCtrl

    constructor: (@$log, @$scope, @MessageService, @$state, @$stateParams, @Message, @UtilityService, @ErrorService) ->
        @$log.debug "constructing MessageCtrl"
        @messageBoxes = []
        @messages = []
        @filterMessageBoxId
        # load list of messages from server
        @listMessageBoxes()
        @listMessages()
    
    listMessages: () ->
        @$log.debug "MessageCtrl.listMessages()"
        @Message.query().$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} messages"
                @messages = data
            ,
            (error) =>
                @$log.error "Unable to get messages: #{error}"
                @ErrorService.error("Unable to fetch messages from server!")
            )

    listMessageBoxes: () ->
        @$log.debug "MessageCtrl.listMessageBoxes()"
        @MessageService.listMessageBoxes().then(
            (data) =>
                @$log.debug "Promise returned #{data.length} messages"
                @messageBoxes = data
                msgBox = @UtilityService.findByProperty(data, 'name', 'Messages')
                @filterMessageBoxId = msgBox.messageBoxId
            ,
            (error) =>
                @$log.error "Unable to get messages: #{error}"
                @ErrorService.error("Unable to fetch message box information from server!")
            )

    filterMessage: (messageBoxId) ->
      @$log.debug "MessageCtrl.filterMessage(#{messageBoxId})"
      @filterMessageBoxId = messageBoxId

controllersModule.controller('MessageCtrl', MessageCtrl)