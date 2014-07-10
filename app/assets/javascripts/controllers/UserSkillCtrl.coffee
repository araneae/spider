
class UserSkillCtrl

    constructor: (@$log, @UserSkillService, @UserSkill, @$location) ->
        @$log.debug "constructing UserSkillCtrl"
        @userSkills = []
        # fetch data from server
        @listUserSkills()

    listUserSkills: () ->
        @$log.debug "UserSkillCtrl.listUserSkills()"
        @UserSkill.query().$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} my skills"
                @userSkills = data
                @$location.path('/userSkillEmpty') if @userSkills.length is 0
            ,
            (error) =>
                @$log.error "Unable to get my skills: #{error}"
            )

    create: () ->
        @$log.debug "UserSkillCtrl.create()"
        @$location.path('/userSkillCreate')

    edit: (skillId) ->
        @$log.debug "UserSkillCtrl.edit(#{skillId})"
        @$location.path("/userSkillEdit/#{skillId}")

controllersModule.controller('UserSkillCtrl', UserSkillCtrl)