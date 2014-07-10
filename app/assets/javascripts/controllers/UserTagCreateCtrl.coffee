
class UserTagCreateCtrl

    constructor: (@$log, @UserTag, @$scope, @$state, @$stateParams, @$location) ->
        @$log.debug "constructing UserTagCreateCtrl"
        @userTag = {}

    create: () ->
        @$log.debug "UserTagCreateCtrl.create()" 
        userTag = new @UserTag(@userTag);
        userTag.$save().then( (data) =>
            @$log.debug "server returned #{data} UserTag"
            parent = @$scope.ctrl
            parent.listUserTags() if parent
            @$state.go('^', @$stateParams)
        )

    cancel: () ->
        @$log.debug "UserTagCreateCtrl.cancel()"
        @$state.go('^', @$stateParams)

controllersModule.controller('UserTagCreateCtrl', UserTagCreateCtrl)