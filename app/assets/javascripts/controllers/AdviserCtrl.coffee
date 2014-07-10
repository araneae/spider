
class AdviserCtrl

    constructor: (@$log, @AdviserService, @Adviser, @$location) ->
        @$log.debug "constructing AdviserCtrl"
        @myAdvisers = []
        @searchText
        @searchResult = {}
        # fetch data from server
        @listAdvisers()

    listAdvisers: () ->
        @$log.debug "AdviserCtrl.listAdvisers()"
        @Adviser.query().$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} my advisers"
                @myAdvisers = data
            ,
            (error) =>
                @$log.error "Unable to get my advisers: #{error}"
            )

    search: () ->
      @$log.debug "AdviserCtrl.search()"
      
      @AdviserService.search(@searchText).then(
         (data) =>
                @$log.debug "Successfully returned search result #{data}"
                @searchResult = data
         ,
         (error) =>
            @$log.error "Unable to search #{@searchText}"
      )

    invite: () ->
      @$log.debug "AdviserCtrl.invite()"
      @Adviser.save({adviserUserId: @searchResult.id})

    showSearchResult: () ->
        @searchResult.id

controllersModule.controller('AdviserCtrl', AdviserCtrl)