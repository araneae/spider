
class MessageBoxCreateCtrl

    constructor: (@$log, @MessageBox, @$scope, @$state, @$stateParams, @$location, @ErrorService) ->
        @$log.debug "constructing MessageBoxCreateCtrl"
        @messageBox = {}

    create: () ->
        @$log.debug "MessageBoxCreateCtrl.create()" 
        @MessageBox.save(@messageBox).$promise.then( 
          (data) =>
              @$log.debug "Successfully created message box #{data}"
              @$state.go('messages.messageList', @$stateParams, {reload: true})
          ,
          (error) =>
              @$log.debug "Unable to create message box #{error}"
              @ErrorService.error("Unable to create label!")
        )

    cancel: () ->
        @$log.debug "MessageBoxCreateCtrl.cancel()"
        #@$state.go('^', @$stateParams)
        @$state.go('messages.messageList', @$stateParams)

controllersModule.controller('MessageBoxCreateCtrl', MessageBoxCreateCtrl)