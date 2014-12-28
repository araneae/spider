
class UserProfileSettingCtrl

    constructor: (@$log, @$scope, @$state, @ErrorService, @EnumService, @UserProfileSettingService, @UtilityService) ->
        @$log.debug "constructing UserProfileSettingCtrl"
        @userProfile = {}
        @verifyCode
        @emailChangeState = 'UNCHANGED'
        @emailReadOnly = true
        @emailOrg
        @emailVerify = false
        @selectedFile
        @profileImageUrl = "/assets/images/default_user.jpg"
        
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
                @setImageUrl(@userProfile.physicalFile)
            ,
            (error) =>
                @ErrorService.error
                @$log.error "Unable to get user profile: #{error}"
            )
    
    setImageUrl: (picture) ->
        @profileImageUrl = "/userProfile/picture/#{picture}" if (picture)
    
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
                @$log.error "Unable to update email: #{error}"
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

    cancel: () ->
        @$log.debug "UserProfileSettingCtrl.cancel()"
        @$state.go("index")
    
    onFileSelect: ($files) ->
      @$log.debug "UserProfileSettingCtrl.onFileSelect()"
      for file in $files
        @userProfile.pictureFile = file.name
        @selectedFile = file
        break
    
    save: () ->
        @$log.debug "UserProfileSettingCtrl.save()"
        if (@userProfile.pictureFile and @userProfile.pictureFile.length > 0)
          @UtilityService.uploadFile('/userProfile/picture/upload', 'application/text', @selectedFile, @updateUserProfile, @onUploadError)
        else
          @userProfile.pictureFile = undefined
          @updateUserProfile()

    updateUserProfile: (data, status, headers, config) =>
        @$log.debug "UserProfileSettingCtrl.updateUserProfile()"
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

    onUploadError : (error) =>
      @ErrorService.error("Failed to upload picture!")
      @$log.debug "UserProfileSettingCtrl.onUploadError(#{error})"

controllersModule.controller('UserProfileSettingCtrl', UserProfileSettingCtrl)