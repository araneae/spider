
class MessageCtrl

    constructor: (@$log, @$scope, @MessageService, @$state, @$stateParams, @Message, @UtilityService) ->
        @$log.debug "constructing MessageCtrl"
        @messages = []
        # load list of messages from server
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
            )

    markStar: (message) ->
       message.star = true
       @MessageService.markStar(message.messageId)
    
    removeStar: (message) ->
      message.star = false
      @MessageService.removeStar(message.messageId)
    
    markImportant: (message) ->
      message.important = true
      @MessageService.markImportant(message.messageId)
    
    removeImportant: (message) ->
      message.important = false
      @MessageService.removeImportant(message.messageId)

controllersModule.controller('MessageCtrl', MessageCtrl)