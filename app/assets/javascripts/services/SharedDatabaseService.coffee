# Service class for Shared Database model

class SharedDatabaseService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing SharedDatabaseService"

servicesModule.service('SharedDatabaseService', SharedDatabaseService)

# define the factories
#
servicesModule.factory('SharedDocument', ['$resource', ($resource) -> 
              $resource('/database/shared/document/:id', {id: '@id'}, 
                        { 
                          'update': {method: 'PUT'}
                        }
                       )])
