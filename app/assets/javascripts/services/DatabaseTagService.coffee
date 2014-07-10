# Service class for document tag model

class DatabaseTagService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing DatabaseTagService"


servicesModule.service('DatabaseTagService', DatabaseTagService)

# define the factories
#
servicesModule.factory('DocumentTag', ['$resource', ($resource) -> 
              $resource('/database/documentTag/:documentId', 
              {documentId: '@documentId'})])
