# Service class for handing errors

class ErrorService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log) ->
        @$log.debug "constructing ErrorService"
        @message
        @type='success'

    error: (msg) ->
        @$log.debug "ErrorService.error(#{msg})"
        if (msg)
          @message = msg
        else
          @message = "Oops, something wrong. Unable to fetch data from server!"
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
