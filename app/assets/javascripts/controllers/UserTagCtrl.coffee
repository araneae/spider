
class UserTagCtrl

    constructor: (@$log, @$q, @$scope, @$state, @UserTagService, @UtilityService, @UserTag, @DocumentTag, @ErrorService) ->
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
      @$log.debug "UserTagCtrl.isDisabled(#{userTag})"
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
   
    goToFolder: () ->
        @$log.debug "UserTagCtrl.goToFolder()"
        @$state.go("folder.documents")

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
            orgObj = @UtilityService.findByProperty(@userTags, 'userTagId', tag.userTagId)
            equals = angular.equals(folder, orgObj)
            if (!equals)
              promise = @UserTag.update(tag).$promise
              promises.push(promise)
        # wait for all the promises to complete
        if (promises.length > 0)
          @$q.all(promises).then(
            (data) =>
                 @$log.debug "Successfully updated all the tags!"
                 @listUserTags()
                 @$state.go('database.documents')
            ,
            (error) =>
                @$log.debug "one of the promise failed"
                @listUserTags()
                @ErrorService.error("Unable to update tag #{angular.toJson(error)}!")
          )

    onDropComplete: (document, evt, tag) ->
        @$log.debug "UserTagCtrl.onDropComplete(#{document}, #{tag})"
        if (tag.userTagId > 0)
          @DocumentTag.save({documentId: document.documentId, userTagId: tag.userTagId, userDocumentId: document.userDocumentId}).$promise
            .then(
              (data) =>
                  @ErrorService.success("Successfully attached tag #{tag.name} to document #{document.name}.")
                  @$log.debug "Successfully added tag #{data}"
              ,
              (error) =>
                  @ErrorService.error(error.data.message)
                  @$log.debug("Unable to attach tag #{error}.")
            )

controllersModule.controller('UserTagCtrl', UserTagCtrl)