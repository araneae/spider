# Service class for RightBar

class RightBarService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing RightBarService"

    getContents: () ->
        @$log.debug "RightBarService.getContents()"
        deferred = @$q.defer()

        @$http.get("/rightBar/contents")
        .success((data, status, headers) =>
                @$log.info("Successfully fetched right bar contents - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to fetch right bar contents - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

servicesModule.service('RightBarService', RightBarService)

# define the factories
#
