
class IndustryService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing IndustryService"

    deleteIndustry: (industryId) ->
        @$log.debug "IndustryService.delete #{industryId}"
        @$http.delete("/industry/#{industryId}")


servicesModule.service('IndustryService', IndustryService)

# define the factories
#
servicesModule.factory('Industry', ['$resource', ($resource) -> 
              $resource('/industry/:industryId', {industryId: '@industryId'}, {'update': {method: 'PUT'}})])