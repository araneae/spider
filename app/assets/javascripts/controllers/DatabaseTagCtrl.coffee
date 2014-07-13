
class DatabaseTagCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q, @DatabaseTagService, @Document, @UserTag, @DocumentTag, @UtilityService, @$location) ->
        @$log.debug "constructing DatabaseTagCtrl"
        @documentId = parseInt(@$stateParams.documentId)
        @documentTags = []
        @userTags = []
        @document = {}
        @promiseDocumentTags = {}
        @promiseUserTags = {}
        @docTags = []
        # load objects from server
        @loadDocument(@documentId)
        @listDocumentTags(@documentId)
        @loadUserTags()
        @joinAll()

    listDocumentTags: (documentId) ->
        @$log.debug "DatabaseTagCtrl.listDocumentTags()"
        delay = @$q.defer()
        @promiseDocumentTags = @DocumentTag.query({documentId: documentId}).$promise
        @promiseDocumentTags.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} document tags"
                @documentTags = data
            ,
            (error) =>
                @$log.error "Unable to get document tags: #{error}"
            )

    loadDocument: (documentId) ->
        @$log.debug "DatabaseTagCtrl.loadDocument(#{documentId})"
        delay = @$q.defer()
        @Document.get({id: documentId}).$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data} document"
              @document = data
          ,
          (error) =>
              @$log.error "Unable to get document: #{error}"
          )

    loadUserTags: () ->
        @$log.debug "DatabaseTagCtrl.loadUserTags()"
        delay = @$q.defer()
        @promiseUserTags = @UserTag.query().$promise
        @promiseUserTags.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} tags"
                @userTags = data
            ,
            (error) =>
                @$log.error "Unable to get tags: #{error}"
            )

    joinAll: () ->
        @$log.debug "DatabaseTagCtrl.joinAll()"
        Promise.all([@promiseDocumentTags, @promiseUserTags]).then(
            () =>
                 @$log.debug "returned by all promises"
                 for tag in @userTags
                      tagId = @UtilityService.findByProperty(@documentTags, 'userTagId', tag.id)
                      isTagged = !@UtilityService.isEmpty(tagId)
                      obj = new Object()
                      obj["name"] = tag.name
                      obj["userTagId"] = tag.id
                      obj["isTagged"] = isTagged
                      @docTags.push(obj)
                 # update scope to refresh ui
                 @$scope.$apply()
           ,
           () =>
                @$log.debug "one of the promise failed"
        )

    cancel: () ->
      @$log.debug "DatabaseTagCtrl.cancel()"
      @$state.go('database')

    save: () ->
      @$log.debug "DatabaseTagCtrl.save()"
      #tagged = @docTags.filter((element, index, array) =>
      #               element.isTagged == true
      #)
      for newTag in @docTags
        oldTagId = @UtilityService.findByProperty(@documentTags, 'userTagId', newTag.userTagId)
        if (newTag.isTagged && !oldTagId)
            # create documentTag
            docTag = new @DocumentTag({documentId: @documentId, userTagId: newTag.userTagId})
            docTag.$save().then( 
              (data) =>
                @$log.debug "server returned #{data}"
              ,
              (error) =>
                @$log.error "server returned #{error}"
            )
        else if (!newTag.isTagged && oldTagId)
            # delete documentTag
            @DocumentTag.remove({documentId: @documentId, userTagId: oldTagId.userTagId}).$promise.then( 
              (data) =>
                @$log.debug "server returned #{data}"
              ,
              (error) =>
                @$log.error "server returned #{error}"
            )
      @$state.go('database')

controllersModule.controller('DatabaseTagCtrl', DatabaseTagCtrl)