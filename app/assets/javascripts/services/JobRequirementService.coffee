# Service class for JobRequirement model
class JobRequirementService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing JobRequirementService"


servicesModule.service('JobRequirementService', JobRequirementService)

# define the factories
#
servicesModule.factory('JobRequirement', ['$resource', ($resource) -> 
              $resource('/jobRequirement/:jobRequirementId', {jobRequirementId: '@jobRequirementId'}, 
                  {
                    'update': {method: 'PUT'}
                  }
             )])