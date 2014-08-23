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
      deferred = @$q.defer()
      @$http.get("/message/star/mark/#{messageId}")
          .success((data, status, headers) =>
                @$log.info("Successfully added message star mark - status #{status}")
                deferred.resolve(data)
              )
          .error((data, status, headers) =>
                @$log.error("Unable to add message star mark - status #{status}")
                deferred.reject(data);
              )
      deferred.promise
    
    removeStar: (messageId) ->
      @$log.debug "MessageService.removeStar(#{messageId})"
      deferred = @$q.defer()
      @$http.get("/message/star/remove/#{messageId}")
        .success((data, status, headers) =>
                @$log.info("Successfully removed message star mark - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Unable to remove message mark as star - status #{status}")
                deferred.reject(data);
            )
      deferred.promise
    
    markImportant: (messageId) ->
      @$log.debug "MessageService.markImportant(#{messageId})"
      deferred = @$q.defer()
      @$http.get("/message/important/mark/#{messageId}")
        .success((data, status, headers) =>
                @$log.info("Successfully added message important mark - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Unable to add message important mark - status #{status}")
                deferred.reject(data);
            )
      deferred.promise
    
    removeImportant: (messageId) ->
      @$log.debug "MessageService.removeImportant(#{messageId})"
      deferred = @$q.defer()
      @$http.get("/message/important/remove/#{messageId}")
        .success((data, status, headers) =>
                @$log.info("Successfully removed message important mark - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Unable to remove message important mark - status #{status}")
                deferred.reject(data);
            )
      deferred.promise

    markRead: (messageId) ->
      @$log.debug "MessageService.markRead(#{messageId})"
      deferred = @$q.defer()
      @$http.get("/message/read/mark/#{messageId}")
        .success((data, status, headers) =>
                @$log.info("Successfully marked message as read - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Unable to mark message as read - status #{status}")
                deferred.reject(data);
            )
      deferred.promise

    reply: (messageId, body) ->
      @$log.debug "MessageService.reply(#{messageId}, #{body})"
      deferred = @$q.defer()
      @$http.post("/message/reply/#{messageId}", {body: body})
        .success((data, status, headers) =>
                @$log.info("Successfully sent message - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Unable to send message - status #{status}")
                deferred.reject(data);
            )
      deferred.promise

    trash: (messageId) ->
      @$log.debug "MessageService.trash(#{messageId})"
      deferred = @$q.defer()
      @$http.get("/message/trash/#{messageId}")
        .success((data, status, headers) =>
                @$log.info("Successfully trashed message - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Unable to trash message - status #{status}")
                deferred.reject(data);
            )
      deferred.promise

servicesModule.service('MessageService', MessageService)

# define the factories
#
servicesModule.factory('Message', ['$resource', ($resource) -> 
              $resource('/message/:messageId', {messageId: '@messageId'}, 
                        { 
                          'update': {method: 'PUT'}
                        }
                       )])
