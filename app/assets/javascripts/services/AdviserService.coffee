# Service class for Contact model

class AdviserService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing AdviserService"

    search: (searchText) ->
        @$log.debug "AdviserService.search #{searchText}"
        deferred = @$q.defer()

        @$http.get("/adviser/search/#{searchText}")
        .success((data, status, headers) =>
                @$log.info("Successfully searched - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to search - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

servicesModule.service('AdviserService', AdviserService)

# define the factories
#
servicesModule.factory('Adviser', ['$resource', ($resource) -> 
              $resource('/adviser/:adviserUserId', {adviserUserId: '@adviserUserId'}, {'update': {method: 'PUT'}})])
