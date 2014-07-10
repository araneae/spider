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

servicesModule.service('ContactService', ContactService)

# define the factories
#
servicesModule.factory('Contact', ['$resource', ($resource) -> 
              $resource('/contact/:contactUserId', {contactUserId: '@contactUserId'}, {'update': {method: 'PUT'}})])
