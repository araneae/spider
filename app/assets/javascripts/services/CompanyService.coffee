# Service class for Company model
class CompanyService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing CompanyService"


servicesModule.service('CompanyService', CompanyService)

# define the factories
#
servicesModule.factory('Company', ['$resource', ($resource) -> 
              $resource('/company/:companyId', {companyId: '@companyId'}, {'update': {method: 'PUT'}})])