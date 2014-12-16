# Service class for MenuBar

class MenuBarService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing MenuBarService"

    getContents: () ->
        @$log.debug "MenuBarService.getContents()"
        deferred = @$q.defer()

        @$http.get("/menuBar/contents")
        .success((data, status, headers) =>
                @$log.info("Successfully fetched menu bar contents - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to fetch menu bar contents - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

servicesModule.service('MenuBarService', MenuBarService)

# define the factories
#
