
class ConfigService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing ConfigService"
        @documentTablePageSize = 12
        @messageTablePageSize = 12

servicesModule.service('ConfigService', ConfigService)

# define the factories
#
