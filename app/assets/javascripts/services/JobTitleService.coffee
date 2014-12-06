# Service class for JobTitle model
class JobTitleService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing JobTitleService"


servicesModule.service('JobTitleService', JobTitleService)

# define the factories
#
servicesModule.factory('JobTitle', ['$resource', ($resource) -> 
              $resource('/jobTitle/:jobTitleId', {jobTitleId: '@jobTitleId'}, {'update': {method: 'PUT'}})])