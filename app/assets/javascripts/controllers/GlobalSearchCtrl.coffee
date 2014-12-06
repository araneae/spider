
class GlobalSearchCtrl

    constructor: (@$log, @$rootScope) ->
        @$log.debug "constructing GlobalSearchCtrl"
        @searchText

    search: () ->
        @$log.debug "GlobalSearchCtrl.search()"
        @$rootScope.$broadcast('globalSearch', {searchText: @searchText})
        
    clearSearchText: () ->
      @searchText=""
      @$rootScope.$broadcast('globalSearch', {searchText: @searchText})

controllersModule.controller('GlobalSearchCtrl', GlobalSearchCtrl)