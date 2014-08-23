
class MessageBoxCreateCtrl

    constructor: (@$log, @MessageBox, @$scope, @$state, @$stateParams, @$location) ->
        @$log.debug "constructing MessageBoxCreateCtrl"
        @messageBox = {}

    create: () ->
        @$log.debug "MessageBoxCreateCtrl.create()" 
        @MessageBox.save(@messageBox).$promise.then( 
          (data) =>
              @$log.debug "Successfully created message box #{data}"
              @$state.go('messages', @$stateParams, {reload: true})
          ,
          (error) =>
              @$log.debug "Unable to create message box #{error}"
        )

    cancel: () ->
        @$log.debug "MessageBoxCreateCtrl.cancel()"
        #@$state.go('^', @$stateParams)
        @$state.go('messages', @$stateParams)

controllersModule.controller('MessageBoxCreateCtrl', MessageBoxCreateCtrl)