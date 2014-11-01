
class UserTagCtrl

    constructor: (@$log, @$q, @$scope, @$state, @UserTagService, @UtilityService, @UserTag, @ErrorService) ->
        @$log.debug "constructing UserTagCtrl"
        @userTags = []
        @userTagsMgm = []
        @removedIds = []
        # fetch data from server
        @listUserTags()

    listUserTags: () ->
        @$log.debug "UserTagCtrl.listUserTags()"
        @UserTag.query().$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} tags"
                @userTags = data
                @userTags.unshift({userTagId: 0, name: 'All'})
            ,
            (error) =>
                @$log.error "Unable to get tags: #{error}"
                @ErrorService.error("Unable to fetch tags from server!")
            )
  
    isDisabled: (userTag) ->
      @$log.debug "UserTagCtrl.isEditable(#{userTag})"
      userTag.userTagId == 0
      
    goToTag: (userTagId) ->
        @$log.debug "UserTagCtrl.goToTag(#{userTagId})"
        @$state.go("database.documents", {userTagId: userTagId})

    goToUserTagManagement: () ->
        @$log.debug "UserTagCtrl.goToUserTagManagement()"
        @userTagsMgm = angular.copy(@userTags)
        @$state.go("database.userTagManagement")

    goToUserTagCreate: () ->
        @$log.debug "UserTagCtrl.goToUserTagCreate()"
        @$state.go("database.userTagCreate")

    showUserTagManagement: () ->
        @$log.debug "UserTagCtrl.showUserTagManagement()"
        @userTags.length > 0

    cancel: () ->
        @$log.debug "UserTagCtrl.cancel()"
        @$state.go("database.documents")

    remove: (userTagId) ->
        @$log.debug "UserTagCtrl.remove(#{userTagId})"
        @removedIds.push(userTagId)
        index = @UtilityService.findIndexByProperty(@userTagsMgm, 'userTagId', userTagId)
        @userTagsMgm.splice(index, 1)

    save: () ->
        @$log.debug "UserTagCtrl.save()"
        promises = []
        for userTagId in @removedIds
            promise = @UserTag.remove({userTagId: userTagId}).$promise
            promises.push(promise)
        
        # TBD: only update the dirty ones
        for tag in @userTagsMgm
            promise = @UserTag.update(tag).$promise
            promises.push(promise)
        # wait for all the promises to complete
        @$q.all(promises).then(
            () =>
                 @$log.debug "Successfully updated all the tags!"
                 @listUserTags()
                 @$state.go('database.documents')
           ,
           () =>
                @$log.debug "one of the promise failed"
                @listUserTags()
                @ErrorService.error("Unable to update tag!")
        )

controllersModule.controller('UserTagCtrl', UserTagCtrl)