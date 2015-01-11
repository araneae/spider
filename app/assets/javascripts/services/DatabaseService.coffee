# Service class for Database model

class DatabaseService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$upload, @$q) ->
        @$log.debug "constructing DatabaseService"

    search: (userTagId, searchText) ->
        @$log.debug "DatabaseService.search(#{userTagId}, #{searchText})"
        deferred = @$q.defer()

        @$http.get("/database/userTag/#{userTagId}/search/#{searchText}")
        .success((data, status, headers) =>
                @$log.info("Successfully searched - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to search - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

    searchDocument: (documentId) ->
        @$log.debug "DatabaseService.searchDocument(#{documentId})"
        deferred = @$q.defer()

        @$http.get("/database/document/#{documentId}/search")
        .success((data, status, headers) =>
                @$log.info("Successfully searched - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to search - status #{status}")
                deferred.reject(data)
            )
        deferred.promise
    
    getDocumentContents: (documentId) ->
        @$log.debug "DatabaseService.getDocumentContents(#{documentId})"
        deferred = @$q.defer()

        @$http.get("/database/document/#{documentId}/contents")
        .success((data, status, headers) =>
                @$log.info("Successfully searched - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to search - status #{status}")
                deferred.reject(data)
            )
        deferred.promise
   
    getDocumentByUserTagId: (userTagId) ->
        @$log.debug "DatabaseService.getDocumentByUserTagId(#{userTagId})"
        deferred = @$q.defer()
        
        @$http.get("/database/document/usertag/#{userTagId}")
           .success((data, status, headers) =>
                 @$log.info("Successfully searched - status #{status}")
                 deferred.resolve(data)
           )
           .error((data, status, headers) =>
                 @$log.error("Failed to search - status #{status}")
                 deferred.reject(data)
           )
        deferred.promise

    getDocumentByDocumentFolderId: (documentFolderId) ->
        @$log.debug "DatabaseService.getDocumentByDocumentFolderId(#{documentFolderId})"
        deferred = @$q.defer()
        
        @$http.get("/database/document/folder/#{documentFolderId}")
           .success((data, status, headers) =>
                 @$log.info("Successfully searched - status #{status}")
                 deferred.resolve(data)
           )
           .error((data, status, headers) =>
                 @$log.error("Failed to search - status #{status}")
                 deferred.reject(data)
           )
        deferred.promise

    getShareContacts: (documentId) ->
        @$log.debug "DatabaseService.getContacts(#{documentId})"
        deferred = @$q.defer()
        @$http.get("/database/document/#{documentId}/contact")
        .success((data, status, headers) =>
                @$log.info("Successfully fetched contacts - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to fetch contacts - status #{status}")
                deferred.reject(data)
            )
        deferred.promise
    
    copyDocument: (documentId) ->
        @$log.debug " DatabaseService.copyDocument(#{documentId})"
        deferred = @$q.defer()
        @$http.post("/database/document/#{documentId}/copy")
        .success((data, status, headers) =>
                @$log.info("Successfully copied document - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to copy document - status #{status}")
                deferred.reject(data)
            )
        deferred.promise
    
    share: (documentId, share) ->
        @$log.debug "DatabaseService.share(#{documentId}, #{share})"
        deferred = @$q.defer()
        @$http.post("/database/document/#{documentId}/share", share)
        .success((data, status, headers) =>
                @$log.info("Successfully shared contacts - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to share contacts - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

    updateShare: (documentId, share) ->
        @$log.debug "DatabaseService.updateShare(#{documentId}, #{share})"
        deferred = @$q.defer()
        @$http.put("/database/document/#{documentId}/share", share)
        .success((data, status, headers) =>
                @$log.info("Successfully updated shares - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to update shares - status #{status}")
                deferred.reject(data)
            )
        deferred.promise

servicesModule.service('DatabaseService', DatabaseService)

# define the factories
#
servicesModule.factory('Document', ['$resource', ($resource) -> 
              $resource('/database/document/:documentId', {documentId: '@documentId'}, 
                        { 
                          'update': {method: 'PUT'}
                        }
                       )])
