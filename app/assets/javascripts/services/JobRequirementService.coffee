# Service class for JobRequirement model
class JobRequirementService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing JobRequirementService"

    search: (jobSearch) ->
        @$log.debug "JobRequirementService.advanceSearch(#{jobSearch})"
        deferred = @$q.defer()

        @$http.post("/jobRequirement/search", jobSearch)
        .success((data, status, headers) =>
                @$log.info("Successfully searched - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to search - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    post: (jobRequirementId) ->
        @$log.debug "JobRequirementService.post(#{jobRequirementId})"
        deferred = @$q.defer()

        @$http.put("/jobRequirement/#{jobRequirementId}/status/post")
        .success((data, status, headers) =>
                @$log.info("Successfully changed status to posted - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to change status to posted - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    makeDraft: (jobRequirementId) ->
        @$log.debug "JobRequirementService.post(#{jobRequirementId})"
        deferred = @$q.defer()

        @$http.put("/jobRequirement/#{jobRequirementId}/status/draft")
        .success((data, status, headers) =>
                @$log.info("Successfully changed status to draft - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to change status to draft - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    getPreview: (jobRequirementId) ->
        @$log.debug "JobRequirementService.getPreview(#{jobRequirementId})"
        deferred = @$q.defer()

        @$http.get("/jobRequirement/#{jobRequirementId}/preview")
        .success((data, status, headers) =>
                @$log.info("Successfully fetched job for preview - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to fetch job for preview - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

servicesModule.service('JobRequirementService', JobRequirementService)

# define the factories
#
servicesModule.factory('JobRequirement', ['$resource', ($resource) -> 
              $resource('/jobRequirement/:jobRequirementId', {jobRequirementId: '@jobRequirementId'}, 
                  {
                    'update': {method: 'PUT'}
                  }
             )])