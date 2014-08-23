# Directive for showing a message
#
# Usage example: <message-bar ng-model="myMessageVar"></message-bar>
#

class MessageBarDirective
  
  constructor: (@$log, @$rootScope, @Message, @MessageService, @UtilityService) ->

  restrict: 'E'
  
  require: '^ngModel'
  
  templateUrl: '/assets/partials/messageBar.html'

  link: (scope, element, attrs) =>
    modelAttr = attrs.ngModel
    scope.message
    scope.isShowingBody=false
    scope.isShowingReply=false
    scope.replyBody=""
    # watch model variable for change
    scope.$watch(modelAttr, (newVal) =>
                            scope.message = newVal
                )

    scope.toggleDetail = () =>
      @$log.debug "scope.showDetail()"
      scope.isShowingBody = !scope.isShowingBody
      scope.isShowingReply=false
      scope.replyBody=""
      if !scope.message.read
         @MessageService.markRead(scope.message.messageId)
         scope.message.read = true
      if scope.isShowingBody
         @$rootScope.$broadcast('clickedOnMessage', {messageId: scope.message.messageId})

    scope.isShowingDetail = () =>
      scope.isShowingBody

    scope.reply = () =>
      scope.replyBody=""
      scope.isShowingReply=true
   
    scope.discard = () =>
      scope.replyBody=""
      scope.isShowingReply=false
    
    scope.isInTrash = () =>
      scope.message.messageBoxType == 'TRASH'
    
    scope.isInSentItems = () =>
      scope.message.messageBoxType == 'SENTITEMS'
    
    scope.$on('clickedOnMessage', (event, data) =>
                                    @$log.debug "received message clickedOnMessage(#{data.messageId})"
                                    if data.messageId != scope.message.messageId
                                        scope.isShowingBody=false
                                        scope.isShowingReply=false
                                        scope.replyBody=""
              )

    scope.send = () =>
      if !@UtilityService.isEmpty(scope.replyBody)
        scope.isShowingReply=false
        @MessageService.reply(scope.message.messageId, scope.replyBody).then(
            (data) =>
                @$log.debug "Successfully trashed message!"
                scope.$emit('messageReplied', {messageId: scope.message.messageId})
            ,
            (error) =>
                @$log.error "Unable to trash message: #{error}!"
            )

    scope.markStar = () =>
       scope.message.star = true
       @MessageService.markStar(scope.message.messageId)
    
    scope.removeStar = () =>
      scope.message.star = false
      @MessageService.removeStar(scope.message.messageId)
    
    scope.markImportant = () =>
      scope.message.important = true
      @MessageService.markImportant(scope.message.messageId)
    
    scope.removeImportant = () =>
      scope.message.important = false
      @MessageService.removeImportant(scope.message.messageId)

    scope.trash = () =>
      @MessageService.trash(scope.message.messageId).then(
            (data) =>
                @$log.debug "Successfully trashed message!"
                scope.$emit('messageTrashed', {messageId: scope.message.messageId})
            ,
            (error) =>
                @$log.error "Unable to trash message: #{error}!"
            )
    
    scope.remove = () =>
      @Message.remove({messageId: scope.message.messageId}).$promise.then(
            (data) =>
                @$log.debug "Successfully deleted message!"
                scope.$emit('messageRemoved', {messageId: scope.message.messageId})
            ,
            (error) =>
                @$log.error "Unable to delete message: #{error}!"
            )

directivesModule.directive('messageBar', ['$log', '$rootScope', 'Message', 'MessageService', 'UtilityService', 
                                                  ($log, $rootScope, Message, MessageService, UtilityService) ->
                                                    new MessageBarDirective($log, $rootScope, Message, MessageService, UtilityService)
                                        ])
