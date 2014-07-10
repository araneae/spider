# Service class for UserSkill model

class UserSkillService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing UserSkillService"


servicesModule.service('UserSkillService', UserSkillService)

# define the factories
#
servicesModule.factory('UserSkill', ['$resource', ($resource) -> 
              $resource('/userSkill/:skillId', {skillId: '@skillId'}, {'update': {method: 'PUT'}})])

servicesModule.factory('SkillLevel', ['$resource', ($resource) -> $resource('/skillLevel')])