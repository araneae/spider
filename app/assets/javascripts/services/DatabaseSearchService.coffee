# Service class for document tag model

class DatabaseSearchService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing DatabaseSearchService"


servicesModule.service('DatabaseSearchService', DatabaseSearchService)

# define the factories
#
servicesModule.factory('DatabaseSearch', ['$resource', ($resource) -> 
              $resource('/database/documentSearch/:id', 
                        {id: '@id'},
                        { 
                          'update': {method: 'PUT'}
                        })])
