# Service for communicating flash messages among controllers
# example : Displaying success/error message once a user tags a document
# Remember that it doesn't persist the data, so all the data will be
# erased once browser gets refreshed.
#

class FlashService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }
    @data = {}

    constructor: (@$log) ->
        @$log.debug "constructing FlashService"

    set: (name, value) ->
        @$log.debug "FlashService.set(#{name}, #{value})"
        data[name] = value

    get: (name) ->
        @$log.debug "FlashService.get(#{name})"
        data[name]

servicesModule.service('FlashService', FlashService)
