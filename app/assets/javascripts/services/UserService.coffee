
class UserService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing UserService"

servicesModule.service('UserService', UserService)

# define the factories
#

servicesModule.factory('User', ['$resource', ($resource) -> 
              $resource('/user')])

