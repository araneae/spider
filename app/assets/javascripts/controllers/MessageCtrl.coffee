
class MessageCtrl

    constructor: (@$log, @$scope, @DatabaseService, @MessageService, @$state, @$stateParams, @Message, @UtilityService, @ErrorService) ->
        @$log.debug "constructing MessageCtrl"
        @messageBoxes = []
        @inbox={}
        @sentItems={}
        @trash={}
        @newMessage
        @messages = []
        @connections = []
        @select2Options = {
           data : @connections,
           multiple: true
        }
        @filterMessageBoxId
        # load list of messages from server
        @listMessageBoxes()
        @listMessages()
        @loadConnections()
        @$scope.$on('messageTrashed', (event, data) =>
                                    @$log.debug "received message messageTrashed(#{data.messageId})"
                                    @listMessages()
                )
        @$scope.$on('messageRemoved', (event, data) =>
                                    @$log.debug "received message messageRemoved(#{data.messageId})"
                                    @listMessages()
                )
        @$scope.$on('messageReplied', (event, data) =>
                                    @$log.debug "received message messageReplied(#{data.messageId})"
                                    @listMessages()
                )
    
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
    
    loadConnections: () ->
        @$log.debug "MessageCtrl.loadConnections()"
        @DatabaseService.getConnections().then(
            (data) => 
              @$log.debug "Promise returned #{data} connections"
              for obj in data
                @connections.push(obj)
            ,
            (error) =>
              @$log.error "Unable to get connections: #{error}"
              @ErrorService.error("Unable to fetch contacts from server!")
        )

    listMessageBoxes: () ->
        @$log.debug "MessageCtrl.listMessageBoxes()"
        @MessageService.listMessageBoxes().then(
            (data) =>
                @$log.debug "Promise returned #{data.length} messages"
                @inbox = @UtilityService.findByProperty(data, 'messageBoxType', 'INBOX')
                @sentItems = @UtilityService.findByProperty(data, 'messageBoxType', 'SENTITEMS')
                @trash = @UtilityService.findByProperty(data, 'messageBoxType', 'TRASH')
                @filterMessageBoxId = @inbox.messageBoxId if @inbox
                @messageBoxes = []
                for box in data
                   @messageBoxes.push(box) if box.messageBoxType == 'CUSTOM'
            ,
            (error) =>
                @$log.error "Unable to get messages: #{error}"
                @ErrorService.error("Unable to fetch message box information from server!")
            )

    composeMessage: () ->
        @$log.debug "MessageCtrl.composeMessage()"
        @newMessage={}
        @filterMessageBoxId = @inbox.messageBoxId if @filterMessageBoxId != @inbox.messageBoxId

    discard: () ->
        @$log.debug "MessageCtrl.discard()"
        delete @newMessage
    
    send: () ->
        @$log.debug "MessageCtrl.send()"
        @Message.save(@newMessage).$promise.then(
              (data) =>
                  @$log.debug "Successfully sent message!"
                  delete @newMessage
                  @listMessages()
              ,
              (error) =>
                  @$log.error "Unable to send message: #{error}"
            )
  
    filterMessage: (messageBoxId) ->
      @$log.debug "MessageCtrl.filterMessage(#{messageBoxId})"
      @filterMessageBoxId = messageBoxId

controllersModule.controller('MessageCtrl', MessageCtrl)