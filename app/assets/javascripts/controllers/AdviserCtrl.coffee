
class AdviserCtrl

    constructor: (@$log, @AdviserService, @Adviser, @$location, @UtilityService) ->
        @$log.debug "constructing AdviserCtrl"
        @advisers = []
        @searchText
        @searchMode = false
        # fetch data from server
        @listAdvisers()

    listAdvisers: () ->
        @$log.debug "AdviserCtrl.listAdvisers()"
        @searchMode = false
        @searchText
        @Adviser.query().$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} my advisers"
                @advisers = data
            ,
            (error) =>
                @$log.error "Unable to get my advisers: #{error}"
            )

    search: () ->
      @$log.debug "AdviserCtrl.search()"
      
      if @UtilityService.isEmpty(@searchText)
          @listAdvisers()
      else
        @AdviserService.search(@searchText).then(
          (data) =>
                @$log.debug "Successfully returned search result #{data}"
                @searchMode = true
                @advisers = data
          ,
          (error) =>
            @$log.error "Unable to search #{@searchText}"
        )

    invite: (id) ->
      @$log.debug "AdviserCtrl.invite()"
      @Adviser.save({adviserUserId: id}).$promise.then(
          (data) =>
            @$log.debug "Successfully invited #{data}"
            @listAdvisers()
         ,
         (error) =>
            @$log.error "Unable to invite #{@searchText}"
      )

controllersModule.controller('AdviserCtrl', AdviserCtrl)