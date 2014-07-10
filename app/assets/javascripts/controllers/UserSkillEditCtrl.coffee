
class UserSkillEditCtrl

    constructor: (@$log, @$routeParams, @$location, @UserSkillService, @UserSkill, @SkillLevel, @Skill, @UtilityService) ->
        @$log.debug "constructing UserSkillEditCtrl"
        @userSkill = {}
        @selectedSkill = {}
        @skills = []
        @selectedSkillLevel = {}
        @skillLevels = []
        @removeAlert=false
        @paramSkillId = $routeParams.skillId
        # fetch data from server
        @listSkills()
        @listSkillLevels()
        @getUserSkill()

    getUserSkill: () ->
        @$log.debug "UserSkillEditCtrl.getUserSkill()"
        #@userSkill = @UserSkill.get({skillId: @paramSkillId})
        @UserSkill.get({skillId: @paramSkillId}).$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data}"
                @userSkill = data
                @selectedSkill = @UtilityService.findByProperty(@skills, 'id', @userSkill.skillId) if @skills.length > 0
                @selectedSkillLevel = @UtilityService.findByProperty(@skillLevels, 'name', @userSkill.skillLevel) if @skillLevels
            ,
            (error) =>
                @$log.error "Unable to get skill: #{error}"
            )

    listSkills: () ->
        @$log.debug "UserSkillCreateCtrl.listSkills()"
        @Skill.query().$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} skills"
                @skills = data
                @selectedSkill = @UtilityService.findByProperty(@skills, 'id', @userSkill.skillId)  if @userSkill.skillId
            ,
            (error) =>
                @$log.error "Unable to get skills: #{error}"
            )

    listSkillLevels: () ->
        @$log.debug "UserSkillEditCtrl.listSkillLevels()"
        @SkillLevel.query().$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data.length} skill levels"
              @skillLevels = data
              @selectedSkillLevel = @UtilityService.findByProperty(@skillLevels, 'name', @userSkill.skillLevel) if @userSkill.skillLevel 
          ,
          (error) => 
              @$log.error "Unable to get skill levels #{error}"
        )

    update: () ->
        @$log.debug "UserSkillEditCtrl.update()"
        @userSkill.skillLevel = @selectedSkillLevel.name
        @userSkill.skillId = @selectedSkill.id
        @UserSkill.update(@userSkill).$promise.then(
            (data) => 
                  @$log.debug "Updated skill#{data}"
                  @$location.path('/userSkill')
            ,
            (error) =>
                  @$log.error "Unable to update skill #{error}"
        )

    remove: () ->
        @$log.debug "UserSkillEditCtrl.remove()"
        @UserSkill.remove(@userSkill).$promise.then(
            (data) => 
                  @$log.debug "Removed my skill#{data}"
                  @$location.path('/userSkill')
            ,
            (error) =>
                  @$log.error "Unable to remove skill #{error}"
        )

    showRemoveAlert: (value) ->
        @removeAlert = value

    cancel: () ->
        @$log.debug "UserSkillEditCtrl.cancel()"
        @$location.path('/userSkill')

controllersModule.controller('UserSkillEditCtrl', UserSkillEditCtrl)