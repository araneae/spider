
class DatabaseCtrl

    constructor: (@$log, @DatabaseService, @$state, @$stateParams, @Document, @UtilityService, @$location) ->
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
            ,
            (error) =>
                @$log.error "Unable to get documents: #{error}"
            )

    goToDocumentTag: (documentId) ->
        @$log.debug "DatabaseCtrl.goToDocumentTag(#{documentId})"
        doc = @UtilityService.findByProperty(@documents, 'id', documentId)
        @$log.debug "found document #{doc}" if doc
        @$location.path("database/documentTag/#{documentId}")

    goToUpload: () ->
        @$log.debug "DatabaseCtrl.goToUpload()"
        @$state.go("database/upload")

    goToSearch: () ->
        @$log.debug "DatabaseCtrl.goToSearch()"
        @$state.go("database/search")

controllersModule.controller('DatabaseCtrl', DatabaseCtrl)