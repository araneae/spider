# Service class for Database model

class DatabaseService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$http, @$upload, @$q) ->
        @$log.debug "constructing DatabaseService"

    upload: (selectedFile, successFn, errFn, progressFn) ->
        @$log.debug "DatabaseService.upload(#{selectedFile})"
        @$upload.upload({
            url: '/database/upload',
            method: 'POST',
            #headers: {'Content-type': 'application/text'},
            #withCredentials: true,
            #data: {data: selectedFile},
            file: selectedFile,
            # set the file formData name ('Content-Desposition'). Default is 'file'
            # fileFormDataName: myFile, //or a list of names for multiple files (html5).
            # customize how data is added to formData. See #40#issuecomment-28612000 for sample code 
            # formDataAppender: function(formData, key, val){}
        })
        .progress( (evt) =>
           @$log.debug "percent: #{parseInt(100.0 * evt.loaded / evt.total)}"
           progressFn(evt) if progressFn
        )
        .success( (data, status, headers, config) =>
            # file is uploaded successfully
            @$log.debug "#{data}"
            successFn(data, status, headers, config) if successFn
        )
        .error( (err) =>
              @$log.error "error in uploading file #{err}"
              errFn(err) if errFn
        )
        #.then(success, error, progress); 
        #.xhr(function(xhr){xhr.upload.addEventListener(...)})// access and attach any event listener to XMLHttpRequest.

    search: (searchText) ->
        @$log.debug "DatabaseService.search #{searchText}"
        deferred = @$q.defer()

        @$http.get("/database/search/#{searchText}")
        .success((data, status, headers) =>
                @$log.info("Successfully searched - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to search - status #{status}")
                deferred.reject(data);
            )
        deferred.promise

    searchDocument: (documentId, searchText) ->
        @$log.debug "DatabaseService.search(#{documentId} #{searchText})"
        deferred = @$q.defer()

        @$http.get("/database/document/#{documentId}/search/#{searchText}")
        .success((data, status, headers) =>
                @$log.info("Successfully searched - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to search - status #{status}")
                deferred.reject(data);
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
                deferred.reject(data);
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
                 deferred.reject(data);
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
                deferred.reject(data);
            )
        deferred.promise

    copyDocument: (documentId) ->
        @$log.debug " DatabaseService.copyDocument(#{documentId})"
        deferred = @$q.defer()
        @$http.post("/database/document/#{documentId}/copy")
        .success((data, status, headers) =>
                @$log.info("Successfully copy document - status #{status}")
                deferred.resolve(data)
            )
        .error((data, status, headers) =>
                @$log.error("Failed to copy document - status #{status}")
                deferred.reject(data);
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
                deferred.reject(data);
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
                deferred.reject(data);
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
