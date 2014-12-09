# Service class for Company User model
class CompanyUserService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing CompanyUserService"
    
servicesModule.service('CompanyUserService', CompanyUserService)

# define the factories
#

servicesModule.factory('CompanyUser', ['$resource', ($resource) -> 
              $resource('/companyUser/:companyUserId', {companyUserId: '@companyUserId'}, 
                        { 
                          'update': {method: 'PUT'}
                        }
                       )])