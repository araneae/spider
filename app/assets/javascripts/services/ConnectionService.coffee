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

servicesModule.service('ConnectionService', ConnectionService)

# define the factories
#
