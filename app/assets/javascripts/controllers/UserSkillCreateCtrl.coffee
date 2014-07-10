
class UserSkillCreateCtrl

    constructor: (@$log, @UserSkill, @Skill, @SkillLevel, @$location) ->
        @$log.debug "constructing UserSkillCreateCtrl"
        @userSkill = {}
        @skill = []
        @skills = []
        @skillLevel = {}
        @skillLevels = []
        # fetch data from server
        @listSkills()
        @listSkillLevels()

    listSkills: () ->
        @$log.debug "UserSkillCreateCtrl.listSkills()"
        @skills = @Skill.query()
    
    listSkillLevels: () ->
        @$log.debug "UserSkillCreateCtrl.listSkillLevels()"
        @skillLevels = @SkillLevel.query()
        
    create: () ->
        @$log.debug "UserSkillCreateCtrl.create()" 
        userSkill = new @UserSkill(@userSkill);
        userSkill.$save().then( (data) =>
            @$log.debug "server returned #{data} Skill"
            @$location.path('/userSkill')
        )

    skillChanged: () -> @userSkill.skillId = @skill.id

    skillLevelChanged: () -> @userSkill.skillLevel = @skillLevel.name

    cancel: () ->
        @$log.debug "UserSkillCreateCtrl.cancel()"
        @$location.path('/userSkill')

controllersModule.controller('UserSkillCreateCtrl', UserSkillCreateCtrl)