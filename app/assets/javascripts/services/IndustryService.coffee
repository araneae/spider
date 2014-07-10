
class IndustryService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing IndustryService"

    listIndustries: () ->
        @$log.debug "IndustryService.listIndustries()"
        deferred = @$q.defer()

        @$http.get("/industry")
        .success((data, status, headers) =>
                @$log.info("Successfully listed #{data.length} Industries - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to list Industries - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    createIndustry: (industry) ->
        @$log.debug "IndustryService.createIndustry #{angular.toJson(industry, true)}"
        deferred = @$q.defer()

        @$http.post('/industry', industry)
        .success((data, status, headers) =>
                @$log.info("Successfully created Industry - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to create industry - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    updateIndustry: (industry) ->
        @$log.debug "IndustryService.updateIndustry #{angular.toJson(industry, true)}"
        deferred = @$q.defer()

        @$http.put('/industry', industry)
        .success((data, status, headers) =>
                @$log.info("Successfully updated Industry - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to update industry - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    deleteIndustry: (id) ->
        @$log.debug "IndustryService.delete #{id}"
        @$http.delete("/industry/#{id}")


servicesModule.factory('Industry', ['$resource',  
            ($resource) ->
                $resource('/industry/:id', {id: '@id'})
])

servicesModule.factory('MultiIndustryLoader', ['Industry', '$q', 
      (Industry, $q) -> 
              () -> 
                delay = $q.defer()
                Industry.query(
                      (industry) -> 
                          delay.resolve(industry)
                      ,
                      () -> 
                          delay.reject('Unable to fetch recipes')
                      )
                delay.promise
])

servicesModule.service('IndustryService', IndustryService)
