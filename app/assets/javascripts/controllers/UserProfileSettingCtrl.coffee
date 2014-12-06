
class UserProfileSettingCtrl

    constructor: (@$log, @$scope, @$state, @ErrorService, @EnumService, @UserProfileSettingService) ->
        @$log.debug "constructing UserProfileSettingCtrl"
        @userProfile = {}
        @verifyCode
        @emailChangeState = 'UNCHANGED'
        @emailReadOnly = true
        @emailOrg
        @emailVerify = false
        
        # method to be used in "watch" method
        @$scope.inputEmail = () =>  
            @userProfile.email

        # watching for any change in "email"
        @$scope.$watch('inputEmail()', (newVal) =>
                       @emailChangeState = 'UNCHANGED' if @userProfile.email is @emailOrg
                       @emailChangeState = 'CHANGED' if @userProfile.email isnt @emailOrg
                      )

        # fetch data from server
        @loadUserProfile()

    loadUserProfile: () ->
        @$log.debug "UserProfileSettingCtrl.loadUserProfile()"
        @UserProfileSettingService.getUserProfile().then(
            (data) =>
                @$log.debug "Promise returned #{data} user profile"
                @emailOrg = data.email 
                @userProfile = data
            ,
            (error) =>
                @ErrorService.error
                @$log.error "Unable to get documents: #{error}"
            )
    
    changeEmail: () ->
        @$log.debug "UserProfileSettingCtrl.changeEmail()"
        @emailReadOnly = false

    isEmailReadOnly: () ->
      @emailReadOnly
      
    isShowChangeButton: () ->
      @emailReadOnly
    
    isShowSendCodeButton: () ->
      @emailChangeState is 'CHANGED'
    
    isShowVerifyCode: () ->
      @emailChangeState is 'SENT_EMAIL'
   
    isShowUpdateButton: () ->
      @emailChangeState is 'SENT_EMAIL'
    
    maxYear: () ->
      new Date().getFullYear()
    
    sendEmailVerificationCode: () ->
        @$log.debug "UserProfileSettingCtrl.sendEmailVerificationCode()"
        @UserProfileSettingService.sendEmailVerificationCode(@userProfile.email).then(
            (data) =>
                @$log.debug "Promise returned #{data} user profile"
                @emailChangeState = 'SENT_EMAIL'
                @ErrorService.success(data.message)
            ,
            (error) =>
                @ErrorService.error("Unable to send validation email!")
                @$log.error "Unable to get documents: #{error}"
            )

    updateEmail: () ->
      @$log.debug "UserProfileSettingCtrl.updateEmail()"
      @UserProfileSettingService.updateEmail(@verifyCode, @userProfile.email).then(
          (data) =>
              @$log.debug "Promise returned #{data}"
              @emailOrg = @userProfile.email
              @emailChangeState = 'UNCHANGED'
              @emailReadOnly = true
              @ErrorService.success(data.message)
          ,
          (error) =>
              @ErrorService.error("Unable to update email!")
              @$log.error "Unable to update email: #{error}"
          )

    save: () ->
        @$log.debug "UserProfileSettingCtrl.save()"
        @userProfile.birthYear = parseInt(@userProfile.birthYear) if @userProfile.birthYear
        @userProfile.birthMonth = parseInt(@userProfile.birthMonth) if @userProfile.birthMonth
        @userProfile.birthDay = parseInt(@userProfile.birthDay) if @userProfile.birthDay
        
        @UserProfileSettingService.save(@userProfile).then( 
            (data) =>
              @ErrorService.success("Successfully saved profile settings")
              @$log.debug "Promise returned #{data}"
              @$state.go("index")
            ,
            (error) =>
                @ErrorService.error
                @$log.error "Unable to save user profile"
            )

    cancel: () ->
        @$log.debug "UserProfileSettingCtrl.cancel()"
        @$state.go("industry")

controllersModule.controller('UserProfileSettingCtrl', UserProfileSettingCtrl)