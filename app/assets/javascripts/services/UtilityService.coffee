# Service with utility methods

class UtilityService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }
    @data = {}

    constructor: (@$log) ->
        @$log.debug "constructing UtilityService"

    findByProperty: (list, prop, value) ->
       for obj in list
           return obj if obj[prop] is value
       return {}

    isEmpty: (obj) ->
      empty = true
      if (obj)
        for prop in obj
          empty = false
      empty
      
servicesModule.service('UtilityService', UtilityService)
