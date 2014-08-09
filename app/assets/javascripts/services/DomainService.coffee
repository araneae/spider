# Service class for Domain model
class DomainService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing DomainService"


servicesModule.service('DomainService', DomainService)

# define the factories
#
servicesModule.factory('Domain', ['$resource', ($resource) -> 
              $resource('/domain/:domainId', {domainId: '@domainId'}, {'update': {method: 'PUT'}})])