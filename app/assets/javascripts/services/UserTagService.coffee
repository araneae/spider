# Service class for UserTag model

class UserTagService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing UserTagService"


servicesModule.service('UserTagService', UserTagService)

# define the factories
#
servicesModule.factory('UserTag', ['$resource', ($resource) -> 
              $resource('/userTag/:id', {id: '@id'}, {'update': {method: 'PUT'}})])
