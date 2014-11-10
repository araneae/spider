
class SkillCtrl

    constructor: (@$log, @SkillService, @IndustryService, @UtilityService) ->
        @$log.debug "constructing SkillController"
        @skill = {}
        @skills = []
        @editMode = false
        @industry = {}
        @industries = []
        # fetch data from server
        @listIndustries()
        @listSkills();

    listIndustries: () ->
        @$log.debug "listIndustries()"
        @editMode = false
        @IndustryService.listIndustries()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Industries"
                @industries = data
            ,
            (error) =>
                @$log.error "Unable to get Industries: #{error}"
            )
    
    listSkills: () ->
        @$log.debug "listSkills()"
        @editMode = false
        @skill = {}
        @industry = {}
        @SkillService.listSkills()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Skills"
                @skills = data
            ,
            (error) =>
                @$log.error "Unable to get Skills: #{error}"
            )
    
    create: () ->
        @$log.debug "create()"
        @editMode = false
        @skill.industryId = @industry.id
        @SkillService.createSkill(@skill)
        .then( 
            (data) =>
              @$log.debug "Promise returned #{data} Skill"
              @listSkills()
            ,
            (error) =>
                @$log.error "Unable to create Skill: #{error}"
            )

    update: () ->
        @$log.debug "update()"
        @editMode = false
        @skill.industryId = @industry.id
        @SkillService.updateSkill(@skill)
        .then( 
            (data) =>
              @$log.debug "Promise returned #{data} Skill"
              @listSkills()
            ,
            (error) =>
                @$log.error "Unable to save Skill: #{error}"
            )

    save: () ->
        if @editMode
            @update()
         else
            @create()

    cancel: () ->
        @$log.debug "cancel()"
        @skill = {}
        @editMode = false
    
    delete: (index) ->
        @$log.debug "delete(#{index})"
        item = @skills[index]
        @editMode = false
        @SkillService.deleteSkill(item.id)
        .then( (data) =>
            @$log.debug "server returned #{data} Skill"
            @listSkills()
        )
    
    edit: (index) ->
        @$log.debug "edit(#{index})"
        item = @skills[index]
        @skill = item
        @industry = @UtilityService.findByProperty(@industries, 'id', item['industryId'])
        @editMode = true
    
    getCreateLabel: () ->
        if @editMode
          'Edit Skill'
        else
           'Create Skill'

controllersModule.controller('SkillCtrl', SkillCtrl)