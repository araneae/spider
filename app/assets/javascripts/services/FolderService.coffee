# Service class for DocumentFolder model

class FolderService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$q) ->
        @$log.debug "constructing FolderService"

    moveToFolder: (documentId, documentFolderId) ->
        @$log.debug "FolderService.moveToFolder(#{documentId}, #{documentFolderId})"
        deferred = @$q.defer()

        @$http.put("/database/document/#{documentId}/move/#{documentFolderId}")
        .success((data, status, headers) =>
                @$log.info("Successfully updated - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to update - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    getFolderShareContacts: (documentFolderId) ->
        @$log.debug "FolderService.getFolderShareContacts(#{documentFolderId})"
        deferred = @$q.defer()
        @$http.get("/documents/folder/contact/#{documentFolderId}")
        .success((data, status, headers) =>
                @$log.info("Successfully fetched contacts - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to fetch contacts - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    shareFolder: (documentFolderId, shares) ->
        @$log.debug "FolderService.shareFolder(#{documentFolderId}, #{shares})"
        deferred = @$q.defer()
        @$http.post("/documents/folder/#{documentFolderId}/share", shares)
        .success((data, status, headers) =>
                @$log.info("Successfully shared repository - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to share repository - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    search: (documentFolderId, searchText) ->
        @$log.debug "FolderService.search(#{documentFolderId}, #{searchText})"
        deferred = @$q.defer()

        @$http.get("/documents/folder/#{documentFolderId}/search/#{searchText}")
        .success((data, status, headers) =>
                @$log.info("Successfully searched - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to search - status #{status}")
                deferred.reject(data);
            )
        deferred.promise
    
    search: (searchText) ->
        @$log.debug "FolderService.search(#{searchText})"
        deferred = @$q.defer()

        @$http.get("/documents/folder/search/#{searchText}")
        .success((data, status, headers) =>
                @$log.info("Successfully searched - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to search - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

servicesModule.service('FolderService', FolderService)

# define the factories
#
servicesModule.factory('DocumentFolder', ['$resource', ($resource) -> 
              $resource('/documents/folder/:documentFolderId', {documentFolderId: '@documentFolderId'}, {'update': {method: 'PUT'}})])
