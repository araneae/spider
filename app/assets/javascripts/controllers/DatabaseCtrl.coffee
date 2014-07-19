
class DatabaseCtrl

    constructor: (@$log, @$scope, @DatabaseService, @$state, @$stateParams, @Document, @UtilityService) ->
        @$log.debug "constructing DatabaseCtrl"
        @userTagId
        @userTagId = parseInt(@$stateParams.userTagId) if @$stateParams.userTagId
        @documents = []
        @searchText
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
        @$state.go("database.documents.documentTag", {documentId: documentId})

    goToDocumentEdit: (documentId) ->
        @$log.debug "DatabaseCtrl.goToDocumentEdit(#{documentId})"
        doc = @UtilityService.findByProperty(@documents, 'id', documentId)
        @$log.debug "found document #{doc}" if doc
        @$state.go("database.documents.documentEdit", {documentId: documentId})

    goToUpload: () ->
        @$log.debug "DatabaseCtrl.goToUpload()"
        @$state.go("databaseUpload")

    goToSearch: () ->
        @$log.debug "DatabaseCtrl.goToSearch()"
        @$state.go("databaseSearch")

    search: () ->
        @$log.debug "DatabaseCtrl.search()"
        if (@searchText)
          @documents = []
          @DatabaseService.search(@searchText).then(
            (data) =>
                @$log.debug "Successfully returned search result #{data.length}"
                @documents = data
            ,
            (error) =>
                @$log.error "Unable to search #{@searchText}"
            )
        else
          @listDocuments()

controllersModule.controller('DatabaseCtrl', DatabaseCtrl)