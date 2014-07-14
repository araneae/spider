
class DatabaseSearchCtrl

    constructor: (@$log, @$state, @DatabaseService, @DatabaseSearch, @Document, @UtilityService, @$location) ->
        @$log.debug "constructing DatabaseSearchCtrl"
        @savedSearchTexts = []
        @searchResults = []
        @searchText
        @searchName
        # load data from server
        @listSavedSearchTexts()

    listSavedSearchTexts: () ->
        @$log.debug "DatabaseSearchCtrl.listSavedSearchTexts()"
        @savedSearchTexts = @DatabaseSearch.query()

    selectedSavedSearch: (text) ->
        @$log.debug "DatabaseSearchCtrl.selectedSavedSearch()"
        @searchResults = []
        @searchText = text
        @search()

    cancel: () ->
        @$log.debug "DatabaseSearchCtrl.cancel()"
        @$state.go('database.documents')

    search: () ->
        @$log.debug "DatabaseSearchCtrl.search()"
        @searchResults = []
        @DatabaseService.search(@searchText).then(
          (data) =>
                @$log.debug "Successfully returned search result #{data.length}"
                @searchResults = data
          ,
          (error) =>
            @$log.error "Unable to search #{@searchText}"
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
            )

controllersModule.controller('DatabaseSearchCtrl', DatabaseSearchCtrl)