
class DatabaseSearchCtrl

    constructor: (@$log, @$state, @DatabaseService, @Document, @$location) ->
        @$log.debug "constructing DatabaseSearchCtrl"
        @documents = []
        @searchText = ""

    cancel: () ->
        @$log.debug "DatabaseSearchCtrl.cancel()"
        @$state.go('database')

    search: () ->
       @$log.debug "DatabaseSearchCtrl.search()"
       @documents = []
       @DatabaseService.search(@searchText).then(
         (data) =>
                @$log.debug "Successfully returned search result #{data.length}"
                @documents = data
         ,
         (error) =>
            @$log.error "Unable to search #{@searchText}"
        )


controllersModule.controller('DatabaseSearchCtrl', DatabaseSearchCtrl)