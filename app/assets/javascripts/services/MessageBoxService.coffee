# Service class for message box service

class MessageBoxService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
      @$log.debug "constructing MessageBoxService"

    listMessageBoxes: () ->
        @$log.debug "MessageBoxService.listMessageBoxes()"
        deferred = @$q.defer()
        @$http.get("/messagebox")
            .success((data, status, headers) =>
                @$log.info("Successfully fetched message boxes - status #{status}")
                deferred.resolve(data)
                )
            .error((data, status, headers) =>
                @$log.error("Failed to fetch message boxes - status #{status}")
                deferred.reject(data);
                )
        deferred.promise


servicesModule.service('MessageBoxService', MessageBoxService)

# define the factories
#
servicesModule.factory('MessageBox', ['$resource', ($resource) -> 
              $resource('/messagebox/:messageBoxId', {messageBoxId: '@messageBoxId'}, 
                        { 
                          'update': {method: 'PUT'}
                        }
                       )])
