# Service class for connections

class ConnectionService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$upload, @$q) ->
        @$log.debug "constructing ConnectionService"

    getConnections: () ->
        @$log.debug " SharedDatabaseService.getConnections()"
        deferred = @$q.defer()
        @$http.get("/connection")
        .success((data, status, headers) =>
                @$log.info("Successfully fetched connections - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to fetch connections - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    share: (documentId, share) ->
        @$log.debug "DatabaseService.share(#{documentId}, #{share})"
        deferred = @$q.defer()
        @$http.post("/connection/share/document/#{documentId}", share)
        .success((data, status, headers) =>
                @$log.info("Successfully shared in-network connections - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to share in-network connections - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    copyDocument: (documentId) ->
        @$log.debug " SharedDatabaseService.copyDocument(#{documentId})"
        deferred = @$q.defer()
        @$http.get("/connection/copy/document/#{documentId}")
        .success((data, status, headers) =>
                @$log.info("Successfully copy document - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to copy document - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

servicesModule.service('ConnectionService', ConnectionService)

# define the factories
#
