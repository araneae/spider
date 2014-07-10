
class DatabaseCtrl

    constructor: (@$log, @DatabaseService, @Document, @UtilityService, @$location) ->
        @$log.debug "constructing DatabaseCtrl"
        @documents = []
        # load list of documents from server
        @listDocuments()
    
    listDocuments: () ->
        @$log.debug "DatabaseCtrl.listDocuments()"
        @Document.query().$promise.then(
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

controllersModule.controller('DatabaseCtrl', DatabaseCtrl)