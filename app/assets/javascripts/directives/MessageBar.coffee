# Directive for showing a message
#
# Usage example: <message-bar ng-model="myMessageVar"></message-bar>
#

class MessageBarDirective
  
  constructor: (@$log, @MessageService, @UtilityService) ->

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

    scope.isShowingDetail = () =>
      scope.isShowingBody

    scope.reply = () =>
      scope.replyBody=""
      scope.isShowingReply=true
   
    scope.discard = () =>
      scope.replyBody=""
      scope.isShowingReply=false

    scope.send = () =>
      if !@UtilityService.isEmpty(scope.replyBody)
        scope.isShowingReply=false
        @MessageService.reply(scope.message.messageId, scope.replyBody)

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
      @MessageService.trash(scope.message.messageId)
      
directivesModule.directive('messageBar', ['$log', 'MessageService', 'UtilityService', ($log, MessageService, UtilityService) ->
                                          new MessageBarDirective($log, MessageService, UtilityService)
                                        ])
