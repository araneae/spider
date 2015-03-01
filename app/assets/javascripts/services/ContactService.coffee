# Service class for Contact model

class ContactService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing ContactService"

    search: (searchText) ->
        @$log.debug "ContactService.search #{searchText}"
        deferred = @$q.defer()

        @$http.get("/contact/search/#{searchText}")
        .success((data, status, headers) =>
                @$log.info("Successfully searched - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to search - status #{status}")
                deferred.reject(data);
            )
        deferred.promise
   
    getInviteMessage: (contactUserId) ->
        @$log.debug "ContactService.getInviteMessage #{contactUserId}"
        deferred = @$q.defer()

        @$http.get("/contact/invite/#{contactUserId}")
        .success((data, status, headers) =>
                @$log.info("Successfully fetched data - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to fetch data - status #{status}")
                deferred.reject(data);
            )
        deferred.promise
    
    sendInvite: (contactuserId, contactInvite) ->
        @$log.debug "ContactService.sendInvite #{contactuserId} #{contactInvite}"
        deferred = @$q.defer()

        @$http.post("/contact/invite/#{contactuserId}", contactInvite)
        .success((data, status, headers) =>
                @$log.info("Successfully post data - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to post data - status #{status}")
                deferred.reject(data);
            )
        deferred.promise
    
    accept: (contactuserId) ->
        @$log.debug "ContactService.accept #{contactuserId}"
        deferred = @$q.defer()

        @$http.post("/contact/invite/#{contactuserId}/accept")
        .success((data, status, headers) =>
                @$log.info("Successfully post data - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to post data - status #{status}")
                deferred.reject(data);
            )
        deferred.promise
    
    reject: (contactuserId) ->
        @$log.debug "ContactService.accept #{contactuserId}"
        deferred = @$q.defer()

        @$http.post("/contact/invite/#{contactuserId}/reject")
        .success((data, status, headers) =>
                @$log.info("Successfully post data - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to post data - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    sendSignupInviteEmail: (inviteSignup) ->
        @$log.debug "ContactService.sendSignupInviteEmail (#{inviteSignup})"
        deferred = @$q.defer()

        @$http.post("/contact/invite/signup/email", inviteSignup)
          .success((data, status, headers) =>
                @$log.info("Successfully sent invite email - status #{status}")
                deferred.resolve(data)
            )
          .error((data, status, headers) =>
                @$log.error("Failed to send invite email - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

servicesModule.service('ContactService', ContactService)

# define the factories
#
servicesModule.factory('Contact', ['$resource', ($resource) -> 
              $resource('/contact/:contactUserId', {contactUserId: '@contactUserId'}, {'update': {method: 'PUT'}})])
