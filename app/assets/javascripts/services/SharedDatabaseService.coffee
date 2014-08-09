# Service class for Shared Database model

class SharedDatabaseService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing SharedDatabaseService"

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
        @$log.debug " SharedDatabaseService.share(#{documentId}, #{share})"
        deferred = @$q.defer()
        @$http.post("/connection/share/document/#{documentId}", share)
        .success((data, status, headers) =>
                @$log.info("Successfully shared - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to share - status #{status}")
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

servicesModule.service('SharedDatabaseService', SharedDatabaseService)

# define the factories
#
servicesModule.factory('SharedDocument', ['$resource', ($resource) -> 
              $resource('/database/shared/document/:documentId', {documentId: '@documentId'}, 
                        { 
                          'update': {method: 'PUT'}
                        }
                       )])
