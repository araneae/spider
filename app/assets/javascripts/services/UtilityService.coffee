# Service with utility methods

class UtilityService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log) ->
        @$log.debug "constructing UtilityService"

    findByProperty: (list, prop, value) ->
       for obj in list
           return obj if obj[prop] is value
       return

    findIndexByProperty: (list, prop, value) ->
       index = -1
       for obj in list
           index = index + 1
           return index if obj[prop] is value
       index

    isEmpty: (obj) ->
      empty = true
      if (obj)
        for prop, value of obj
          empty = false
      empty
      
servicesModule.service('UtilityService', UtilityService)
