# Service class for Company model
class CompanyService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing CompanyService"
    
    getCompany: () ->
        @$log.debug "CompanyService:getCompany()"
        deferred = @$q.defer()
        @$http.get("/company")
        .success((data, status, headers) =>
                @$log.info("Successfully fetched company settings - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to search - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    save: (company) ->
        @$log.debug "CompanyService:save(#{company})"
        deferred = @$q.defer()
        @$http.post("/company", company)
        .success((data, status, headers) =>
                @$log.info("Successfully saved company settings - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to save company settings - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

servicesModule.service('CompanyService', CompanyService)

# define the factories
#
