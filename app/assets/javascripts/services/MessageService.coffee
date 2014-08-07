# Service class for message service

class MessageService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing MessageService"

    listMessageBoxes: () ->
        @$log.debug "MessageService.listMessageBoxes()"
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

    markStar: (messageId) ->
       @$log.debug "MessageService.markStar(#{messageId})"
       @$http.get("/message/star/mark/#{messageId}")
    
    removeStar: (messageId) ->
      @$log.debug "MessageService.removeStar(#{messageId})"
      @$http.get("/message/star/remove/#{messageId}")
    
    markImportant: (messageId) ->
      @$log.debug "MessageService.markImportant(#{messageId})"
      @$http.get("/message/important/mark/#{messageId}")
    
    removeImportant: (messageId) ->
      @$log.debug "MessageService.removeImportant(#{messageId})"
      @$http.get("/message/important/remove/#{messageId}")

servicesModule.service('MessageService', MessageService)

# define the factories
#
servicesModule.factory('Message', ['$resource', ($resource) -> 
              $resource('/message/:id', {id: '@id'}, 
                        { 
                          'update': {method: 'PUT'}
                        }
                       )])
