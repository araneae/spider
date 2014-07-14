
class UserTagCreateCtrl

    constructor: (@$log, @UserTag, @$scope, @$state, @$stateParams, @$location) ->
        @$log.debug "constructing UserTagCreateCtrl"
        @userTag = {}

    create: () ->
        @$log.debug "UserTagCreateCtrl.create()" 
        userTag = new @UserTag(@userTag);
        userTag.$save().then( (data) =>
            @$log.debug "server returned #{data} UserTag"
            @$state.go('database.documents')
        )

    cancel: () ->
        @$log.debug "UserTagCreateCtrl.cancel()"
        #@$state.go('^', @$stateParams)
        @$state.go('database.documents', @$stateParams)

controllersModule.controller('UserTagCreateCtrl', UserTagCreateCtrl)