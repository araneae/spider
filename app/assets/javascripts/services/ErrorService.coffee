# Service class for handing errors

class ErrorService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log) ->
        @$log.debug "constructing MessageService"
        @message
        @type='success'

    error: (msg) ->
        @$log.debug "ErrorService.error(#{msg})"
        @message = msg
        @type='error'

    success: (msg) ->
        @$log.debug "ErrorService.success(#{msg})"
        @message = msg
        @type='success'

    clear: () ->
        @$log.debug "ErrorService.clear()"
        @message

servicesModule.service('ErrorService', ErrorService)

# define the factories
#
