
class DatabaseTagCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q, @DatabaseTagService, @Document, @UserTag, @DocumentTag, @UtilityService, @$location, @ErrorService) ->
        @$log.debug "constructing DatabaseTagCtrl"
        @documentId = parseInt(@$stateParams.documentId)
        @documentTags = []
        @userTags = []
        @document = {}
        @promises = []
        @docTags = []
        # load objects from server
        @loadDocument(@documentId)
        @listDocumentTags(@documentId)
        @loadUserTags()
        @joinAll()

    listDocumentTags: (documentId) ->
        @$log.debug "DatabaseTagCtrl.listDocumentTags()"
        delay = @$q.defer()
        docTags = @DocumentTag.query({documentId: documentId}).$promise
        @promises.push(docTags)
        docTags.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} document tags"
                @documentTags = data
            ,
            (error) =>
                @$log.error "Unable to get document tags: #{error}"
                @ErrorService.error("Unable to fetch tags from server!")
            )

    loadDocument: (documentId) ->
        @$log.debug "DatabaseTagCtrl.loadDocument(#{documentId})"
        delay = @$q.defer()
        @Document.get({documentId: documentId}).$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data} document"
              @document = data
          ,
          (error) =>
              @$log.error "Unable to get document: #{error}"
              @ErrorService.error("Unable to fetch data from server!")
          )

    loadUserTags: () ->
        @$log.debug "DatabaseTagCtrl.loadUserTags()"
        delay = @$q.defer()
        userTags = @UserTag.query().$promise
        @promises.push(userTags)
        userTags.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} tags"
                @userTags = data
            ,
            (error) =>
                @$log.error "Unable to get tags: #{error}"
                @ErrorService.error("Unable to fetch tags from server!")
            )

    joinAll: () ->
        @$log.debug "DatabaseTagCtrl.joinAll()"
        #Promise.all(@promises).then(
        @$q.all(@promises).then(
            () =>
                 @$log.debug "returned by all promises"
                 for tag in @userTags
                      tagId = @UtilityService.findByProperty(@documentTags, 'userTagId', tag.userTagId)
                      isTagged = !@UtilityService.isEmpty(tagId)
                      obj = new Object()
                      obj["name"] = tag.name
                      obj["userTagId"] = tag.userTagId
                      obj["isTagged"] = isTagged
                      @docTags.push(obj)
                 # update scope to refresh ui
                 #@$scope.$apply()
           ,
           () =>
                @$log.debug "one of the promise failed"
        )

    cancel: () ->
      @$log.debug "DatabaseTagCtrl.cancel()"
      @$state.go('database.documents')

    save: () ->
      @$log.debug "DatabaseTagCtrl.save()"
      #tagged = @docTags.filter((element, index, array) =>
      #               element.isTagged == true
      #)
      promises=[]
      for newTag in @docTags
        oldTagId = @UtilityService.findByProperty(@documentTags, 'userTagId', newTag.userTagId)
        if (newTag.isTagged && !oldTagId)
            # create documentTag
            docTag = new @DocumentTag({documentId: @documentId, userTagId: newTag.userTagId})
            promise = docTag.$save().$promise
        else if (!newTag.isTagged && oldTagId)
            # delete documentTag
            promise = @DocumentTag.remove({documentId: @documentId, userTagId: oldTagId.userTagId}).$promise
      # wait for all the promises to complete
      @$q.all(promises).then(
            () =>
                 @$log.debug "returned by all promises"
                 @ErrorService.success("Successfully updated tag!")
                 @$state.go('database.documents')
           ,
           () =>
                @$log.debug "one of the promise failed"
                @ErrorService.error("Unable to update tag!")
        )
      

controllersModule.controller('DatabaseTagCtrl', DatabaseTagCtrl)