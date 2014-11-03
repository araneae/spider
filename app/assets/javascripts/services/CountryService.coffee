# Service class for Country model
class CountryService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "Constructing CountryService"


servicesModule.service('CountryService', CountryService)

# define the factories
#
servicesModule.factory('Country', ['$resource', ($resource) -> 
              $resource('/country/:countryId', {countryId: '@countryId'}, {'update': {method: 'PUT'}})])