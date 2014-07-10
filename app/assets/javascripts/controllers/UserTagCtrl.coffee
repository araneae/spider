
class UserTagCtrl

    constructor: (@$log, @UserTagService, @UserTag, @$location) ->
        @$log.debug "constructing UserTagCtrl"
        @userTags = []
        # fetch data from server
        @listUserTags()

    listUserTags: () ->
        @$log.debug "UserTagCtrl.listUserTags()"
        @UserTag.query().$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} tags"
                @userTags = data
            ,
            (error) =>
                @$log.error "Unable to get tags: #{error}"
            )

controllersModule.controller('UserTagCtrl', UserTagCtrl)