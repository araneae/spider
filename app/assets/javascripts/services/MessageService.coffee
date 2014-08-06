# Service class for message service

class MessageService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing MessageService"

    markStar: (messageId) ->
       @$log.debug "IndustryService.markStar(#{messageId})"
       @$http.get("/message/star/mark/#{messageId}")
    
    removeStar: (messageId) ->
      @$log.debug "IndustryService.removeStar(#{messageId})"
      @$http.get("/message/star/remove/#{messageId}")
    
    markImportant: (messageId) ->
      @$log.debug "IndustryService.markImportant(#{messageId})"
      @$http.get("/message/important/mark/#{messageId}")
    
    removeImportant: (messageId) ->
      @$log.debug "IndustryService.removeImportant(#{messageId})"
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
