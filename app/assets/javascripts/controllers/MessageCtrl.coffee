
class MessageCtrl

    constructor: (@$log, @$scope, @ConnectionService, @MessageService, @MessageBox, @$state, @$stateParams, @Message, @UtilityService, @ErrorService) ->
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
        @selectedMessageBoxName
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
        @$scope.$on('messageMoved', (event, data) =>
                                    @$log.debug "received message messageMoved(#{data.messageId}, #{data.messageBoxId})"
                                    @listMessages()
                )
        @$scope.$on('contextMenu', (event, data) =>
                                    @$log.debug "received message contextMenu(#{data.menuItem})"
                                    @refresh() if data.menuItem is "refresh"
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
        @ConnectionService.getConnections().then(
            (data) => 
              @$log.debug "Promise returned #{data} connections"
              for obj in data
                @connections.push(obj)
            ,
            (error) =>
              @$log.error "Unable to get connections: #{error}"
              @ErrorService.error("Unable to fetch contacts from server!")
        )

    refresh: () ->
      @listMessages()
    
    listMessageBoxes: () ->
        @$log.debug "MessageCtrl.listMessageBoxes()"
        @MessageBox.query().$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} messages"
                @inbox = @UtilityService.findByProperty(data, 'messageBoxType', 'INBOX')
                @sentItems = @UtilityService.findByProperty(data, 'messageBoxType', 'SENTITEMS')
                @trash = @UtilityService.findByProperty(data, 'messageBoxType', 'TRASH')
                @filterMessageBoxId = @inbox.messageBoxId if @inbox
                @messageBoxes = []
                for box in data
                   @messageBoxes.push(box)
                # sort the message boxes
                @messageBoxes.sort((a, b) =>
                                        a.name > b.name
                )
                @selectedMessageBoxName = @getMessageBoxName(@filterMessageBoxId)
            ,
            (error) =>
                @$log.error "Unable to get messages: #{error}"
                @ErrorService.error("Unable to fetch message box information from server!")
            )

    getMessageBoxName: (messageBoxId) ->
      for box in @messageBoxes
          return box.name if box.messageBoxId is messageBoxId

    composeMessage: () ->
        @$log.debug "MessageCtrl.composeMessage()"
        @newMessage={}
        @filterMessageBoxId = @inbox.messageBoxId if @filterMessageBoxId != @inbox.messageBoxId
        @selectedMessageBoxName = @getMessageBoxName(@filterMessageBoxId)

    discard: () ->
        @$log.debug "MessageCtrl.discard()"
        delete @newMessage if @newMessage
    
    send: () ->
        @$log.debug "MessageCtrl.send()"
        @Message.save(@newMessage).$promise.then(
              (data) =>
                  @$log.debug "Successfully sent message!"
                  @listMessages()
                  @discard()
              ,
              (error) =>
                  @$log.error "Unable to send message: #{error}"
            )
  
    filterMessage: (messageBoxId) ->
      @$log.debug "MessageCtrl.filterMessage(#{messageBoxId})"
      @filterMessageBoxId = messageBoxId
      @selectedMessageBoxName = @getMessageBoxName(messageBoxId)
      @discard()

    goToLabelCreate: () ->
      @$log.debug "MessageCtrl.goToLabelCreate()"
      @$state.go("messages.createMessageBox")
      
    showMessageBoxManagement: () ->
      @messageBoxes.filter((item) =>
                            item.messageBoxType is 'CUSTOM'
                          ).length > 0
      
    goToMessageBoxManagement: () ->
      @$log.debug "MessageCtrl.goToMessageBoxManagement()"
      @$state.go("messages.manageMessageBox")

controllersModule.controller('MessageCtrl', MessageCtrl)