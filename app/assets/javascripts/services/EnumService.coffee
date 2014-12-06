# Service class for enum types

class EnumService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http) ->
       @$log.debug "constructing EnumService"
       @enumTypes={}
       # fetch enum types
       @loadEnumTypes()

    loadEnumTypes: () ->
        @$log.debug "EnumService.loadEnumTypes()"
        @$http.get("/enum")
            .success((data, status, headers) =>
                @$log.info("Successfully fetched enumTypes - status #{status}")
                @enumTypes=data
              )
          .error((data, status, headers) =>
                @$log.error("Unable to fetch enumTypes - status #{status}")
              )

servicesModule.service('EnumService', EnumService)

# define the factories
#
