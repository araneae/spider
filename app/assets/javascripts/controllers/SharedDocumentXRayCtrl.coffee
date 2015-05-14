
class SharedDocumentXRayCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @$q,
                            @SharedRepositoryService, @UtilityService, @$location, @ErrorService, @UserProfileSettingService) ->
        @$log.debug "constructing SharedDocumentXRayCtrl"
        @documentId = parseInt(@$stateParams.documentId)
        @document = {}
        @searchTerms = ""
        @results = []
        # load objects from server
        @loadUserProfile()
        @loadDocument(@documentId)

    loadDocument: (documentId) ->
        @$log.debug "SharedDocumentXRayCtrl.loadDocument(#{documentId})"
        #delay = @$q.defer()
        @SharedRepositoryService.getDocument(documentId).then(
          (data) =>
              @$log.debug "Promise returned #{data} document"
              @document = data
          ,
          (error) =>
              @ErrorService.error
              @$log.error "Unable to get document: #{error}"
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
        @$log.debug "SharedDocumentXRayCtrl.search()"
        #delay = @$q.defer()
        @SharedRepositoryService.searchDocument(@documentId, @searchTerms).then(
          (data) =>
              @$log.debug "Promise returned #{data} search results"
              @results = data
          ,
          (error) =>
              @ErrorService.error
              @$log.error "Unable to search document: #{error}"
          )

    done: () ->
      @$log.debug "SharedDocumentXRayCtrl.done()"
      @$state.go('sharedRepositories')


controllersModule.controller('SharedDocumentXRayCtrl', SharedDocumentXRayCtrl)