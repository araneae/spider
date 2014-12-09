
class UserProfileSettingService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing UserProfileSettingService"

    getUserProfile: () ->
        @$log.debug "UserProfileSettingService:getUserProfile()"
        deferred = @$q.defer()
        @$http.get("/userProfile")
        .success((data, status, headers) =>
                @$log.info("Successfully fetched user profile - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to search - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    sendEmailVerificationCode: (email) ->
        @$log.debug "UserProfileSettingService:sendEmailVerificationCode(#{email})"
        deferred = @$q.defer()
        @$http.post("/userProfile/email/sendCode", {email: email})
        .success((data, status, headers) =>
                @$log.info("Successfully send email verification code - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to send email verification code - status #{status}")
                deferred.reject(data);
            )
        deferred.promise
    
    updateEmail: (code, email) ->
        @$log.debug "UserProfileSettingService:updateEmail(#{code})"
        deferred = @$q.defer()
        @$http.put("/userProfile/email", {code: code, email: email})
        .success((data, status, headers) =>
                @$log.info("Successfully updated email - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to update email - status #{status}")
                deferred.reject(data);
            )
        deferred.promise
    
    save: (userProfile) ->
        @$log.debug "UserProfileSettingService:save(#{userProfile})"
        deferred = @$q.defer()
        @$http.post("/userProfile", userProfile)
        .success((data, status, headers) =>
                @$log.info("Successfully saved user profile - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to save user profile - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

servicesModule.service('UserProfileSettingService', UserProfileSettingService)

# define the factories
#
