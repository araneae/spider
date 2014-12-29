
class DatabaseSearchCtrl

    constructor: (@$log, @$scope, @$state, @DatabaseService, @DatabaseSearch, @Document, @FolderService,
                              @UtilityService, @$location, @ErrorService) ->
        @$log.debug "constructing DatabaseSearchCtrl"
        @savedSearchTexts = []
        @searchResults = []
        @searchText
        @searchName
        @$scope.$on('globalSearch', (event, data) =>
                                    @$log.debug "received message globalSearch(#{data.searchText})"
                                    @searchText = data.searchText 
                                    @search()
        )
        # load data from server
        @listSavedSearchTexts()
        @listDocuments()

    listSavedSearchTexts: () ->
        @$log.debug "DatabaseSearchCtrl.listSavedSearchTexts()"
        @savedSearchTexts = @DatabaseSearch.query()

    listDocuments: () ->
        @$log.debug "DatabaseSearchCtrl.listDocuments()"
        @DatabaseService.getDocumentByUserTagId(0).then(
            (data) =>
                @$log.debug "Promise returned #{data.length} documents"
                @searchResults = data
            ,
            (error) =>
                @$log.error "Unable to get documents: #{error}"
            )

    selectedSavedSearch: (text) ->
        @$log.debug "DatabaseSearchCtrl.selectedSavedSearch()"
        @searchResults = []
        @searchText = text
        @search()

    done: () ->
        @$log.debug "DatabaseSearchCtrl.done()"
        @UtilityService.goBack('folder.documents')

    search: () ->
        @$log.debug "DatabaseSearchCtrl.search()"
        if (@UtilityService.isStringEmpty(@searchText))
          @listDocuments()
        else
          @searchResults = []
          @FolderService.search(@searchText).then(
            (data) =>
                @$log.debug "Successfully returned search result #{data.length}"
                @searchResults = data
            ,
            (error) =>
              @$log.error "Unable to search #{@searchText}"
              @ErrorService.error("Unable to get search results from server!")
          )

    saveQuery: () ->
        @$log.debug "DatabaseSearchCtrl.search()"
        if (@searchText && @searchName) 
          obj = @UtilityService.findByProperty(@savedSearchTexts, 'name', @searchName)
          if (obj)
            # update
            obj.searchText = @searchText
            @DatabaseSearch.update(obj).$promise.then(
              (data) => 
                      @$log.debug "Successfully updated search #{data}"
                      @listSavedSearchTexts()
              ,
              (error) =>
                      @$log.debug "Unable to save search #{error}"
                      @ErrorService.error("Unable to update query!")
            )
          else
            # save a new search  
            @DatabaseSearch.save({name: @searchName, searchText: @searchText}).$promise.then(
              (data) => 
                      @$log.debug "Successfully saved search #{data}"
                      @listSavedSearchTexts()
              ,
              (error) =>
                      @$log.debug "Unable to save search #{error}"
                      @ErrorService.error("Unable to save query!")
            )

controllersModule.controller('DatabaseSearchCtrl', DatabaseSearchCtrl)