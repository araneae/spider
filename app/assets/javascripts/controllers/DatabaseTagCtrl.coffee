
class DatabaseTagCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q, @DatabaseTagService, @Document, @UserTag, @DocumentTag, @UtilityService, @$location) ->
        @$log.debug "constructing DatabaseTagCtrl"
        @documentId = @$stateParams.documentId
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
        @promiseDocumentTags = @DocumentTag.get({documentId: documentId}).$promise
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
      @$location.path('/database')

controllersModule.controller('DatabaseTagCtrl', DatabaseTagCtrl)