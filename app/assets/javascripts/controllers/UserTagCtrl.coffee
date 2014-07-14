
class UserTagCtrl

    constructor: (@$log, @$state, @UserTagService, @UtilityService, @UserTag) ->
        @$log.debug "constructing UserTagCtrl"
        @userTags = []
        @removedIds = []
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

    goToTag: (userTagId) ->
        @$log.debug "UserTagCtrl.goToTag(#{userTagId})"
        @$state.go("database.documents", {userTagId: userTagId})

    cancel: () ->
        @$log.debug "UserTagCtrl.cancel()"
        @$state.go("database.documents")

    remove: (id) ->
        @$log.debug "UserTagCtrl.remove(#{id})"
        @removedIds.push(id)
        index = @UtilityService.findIndexByProperty(@userTags, 'id', id)
        @userTags.splice(index, 1)

    save: () ->
        @$log.debug "UserTagCtrl.save()"
        for id in @removedIds
            @UserTag.remove({id: id}).$promise.then(
              (data) =>
                  @$log.debug "Successfully deleted tag!"
              ,
              (error) =>
                  @$log.error "Unable to delete tag: #{error}!"
            )
        
        for tag in @userTags
            @UserTag.update(tag).$promise.then(
              (data) =>
                  @$log.debug "Successfully updated tag!"
              ,
              (error) =>
                  @$log.error "Unable to update tag: #{error}!"
            )

controllersModule.controller('UserTagCtrl', UserTagCtrl)