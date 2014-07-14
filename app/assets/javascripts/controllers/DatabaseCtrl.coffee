
class DatabaseCtrl

    constructor: (@$log, @$scope, @DatabaseService, @$state, @$stateParams, @Document, @UtilityService) ->
        @$log.debug "constructing DatabaseCtrl"
        @userTagId
        @userTagId = parseInt(@$stateParams.userTagId) if @$stateParams.userTagId
        @documents = []
        # load list of documents from server
        @listDocuments()
    
    listDocuments: () ->
        @$log.debug "DatabaseCtrl.listDocuments()"
        @Document.query({userTagId: @userTagId}).$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} documents"
                @documents = data
                # update scope to refresh ui
                #@$scope.$apply()
            ,
            (error) =>
                @$log.error "Unable to get documents: #{error}"
            )

    goToDocumentTag: (documentId) ->
        @$log.debug "DatabaseCtrl.goToDocumentTag(#{documentId})"
        doc = @UtilityService.findByProperty(@documents, 'id', documentId)
        @$log.debug "found document #{doc}" if doc
        @$state.go("databaseDocumentTag", {documentId: documentId})

    goToUpload: () ->
        @$log.debug "DatabaseCtrl.goToUpload()"
        @$state.go("databaseUpload")

    goToSearch: () ->
        @$log.debug "DatabaseCtrl.goToSearch()"
        @$state.go("databaseSearch")

controllersModule.controller('DatabaseCtrl', DatabaseCtrl)