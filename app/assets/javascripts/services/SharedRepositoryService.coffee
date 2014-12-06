# Service class for shared repository

class SharedRepositoryService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$upload, @$q) ->
        @$log.debug "constructing SharedRepositoryService"

    getDocuments: () ->
        @$log.debug " SharedRepositoryService.getDocuments()"
        deferred = @$q.defer()
        @$http.get("/shared/repository/document")
        .success((data, status, headers) =>
                @$log.info("Successfully fetched shared documents - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to fetch shared documents - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    getDocument: (documentId) ->
        @$log.debug " SharedRepositoryService.getDocument(#{documentId})"
        deferred = @$q.defer()
        @$http.get("/shared/repository/document/#{documentId}")
        .success((data, status, headers) =>
                @$log.info("Successfully fetched shared document - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to fetch shared document - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    getDocumentContents: (documentId) ->
        @$log.debug " SharedRepositoryService.getDocumentContents(#{documentId})"
        deferred = @$q.defer()
        @$http.get("/shared/repository/document/#{documentId}/contents")
        .success((data, status, headers) =>
                @$log.info("Successfully fetched shared document contents - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to fetch shared document contents - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    searchDocument: (documentId) ->
        @$log.debug "SharedRepositoryService.search(#{documentId})"
        deferred = @$q.defer()

        @$http.get("/shared/document/#{documentId}/search")
        .success((data, status, headers) =>
                @$log.info("Successfully searched - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to search - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    copyDocument: (documentId) ->
        @$log.debug " SharedRepositoryService.copyDocument(#{documentId})"
        deferred = @$q.defer()
        @$http.post("/shared/document/#{documentId}/copy")
        .success((data, status, headers) =>
                @$log.info("Successfully copied document - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to copy document - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

servicesModule.service('SharedRepositoryService', SharedRepositoryService)

# define the factories
#
