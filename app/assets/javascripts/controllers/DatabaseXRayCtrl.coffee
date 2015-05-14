
class DatabaseXRayCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q, @Document, 
                            @DatabaseService, @UtilityService, @$location, @ErrorService, @UserProfileSettingService) ->
        @$log.debug "constructing DatabaseXRayCtrl"
        @documentId = parseInt(@$stateParams.documentId)
        @document = {}
        @searchTerms = ""
        @results = []
        # load objects from server
        @loadUserProfile()
        @loadDocument(@documentId)

    loadDocument: (documentId) ->
        @$log.debug "DatabaseXRayCtrl.loadDocument(#{documentId})"
        #delay = @$q.defer()
        @Document.get({documentId: documentId}).$promise.then(
          (data) =>
              @$log.debug "Promise returned #{data} document"
              @document = data
          ,
          (error) =>
              @$log.error "Unable to get document: #{error}"
              @ErrorService.error("Unable to fetch data from server!")
          )
    
    loadUserProfile: () ->
        @$log.debug "DatabaseXRayCtrl.loadUserProfile()"
        @UserProfileSettingService.getUserProfile().then(
          (data) =>
              @$log.debug "Promise returned user profile"
              #@userProfile = data
              @searchTerms = data.xrayTerms
              @search()
          ,
          (error) =>
              @$log.error "Unable to get user profile: #{error}"
              @ErrorService.error("Unable to fetch data from server!")
          )

    search: () ->
        @$log.debug "DatabaseXRayCtrl.search()"
        @DatabaseService.searchDocument(@documentId, @searchTerms).then(
          (data) =>
              @$log.debug "Promise returned #{data} search results"
              @results = data
          ,
          (error) =>
              @$log.error "Unable to search document: #{error}"
              @ErrorService.error("Unable to fetch data from server!")
          )
    
    done: () ->
      @$log.debug "DatabaseXRayCtrl.done()"
      @UtilityService.goBack('folder.documents')


controllersModule.controller('DatabaseXRayCtrl', DatabaseXRayCtrl)