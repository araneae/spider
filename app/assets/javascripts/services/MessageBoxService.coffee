# Service class for message box service

class MessageBoxService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
      @$log.debug "constructing MessageBoxService"


servicesModule.service('MessageBoxService', MessageBoxService)

# define the factories
#
servicesModule.factory('MessageBox', ['$resource', ($resource) -> 
              $resource('/messagebox/:messageBoxId', {messageBoxId: '@messageBoxId'}, 
                        { 
                          'update': {method: 'PUT'}
                        }
                       )])
