
class SkillService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing SkillService"

    listSkills: () ->
        @$log.debug "SkillService.listSkills()"
        deferred = @$q.defer()

        @$http.get("/skill")
        .success((data, status, headers) =>
                @$log.info("Successfully listed #{data.length} Skills - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to list Skills - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    createSkill: (skill) ->
        @$log.debug "SkillService.createSkill #{angular.toJson(skill, true)}"
        deferred = @$q.defer()

        @$http.post('/skill', skill)
        .success((data, status, headers) =>
                @$log.info("Successfully created Skill - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to create skill - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    updateSkill: (skill) ->
        @$log.debug "SkillService.updateSkill #{angular.toJson(skill, true)}"
        deferred = @$q.defer()

        @$http.put('/skill', skill)
        .success((data, status, headers) =>
                @$log.info("Successfully updated Skill - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to update skill - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    deleteSkill: (skillId) ->
        @$log.debug "SkillService.delete #{skillId}"
        @$http.delete("/skill/#{skillId}")

servicesModule.service('SkillService', SkillService)

# define the factories
#
servicesModule.factory('Skill', ['$resource', ($resource) -> 
              $resource('/skill/:skillId', {skillId: '@skillId'}, {'update': {method: 'PUT'}})])