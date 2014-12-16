
class RightBarCtrl

    constructor: (@$log, @$rootScope, @$state, @RightBarService) ->
        @$log.debug "constructing RightBarCtrl"
        @contents
        
        # fetch data from server
        @loadContents()

    loadContents: () ->
      @$log.debug "RightBarCtrl.loadContents()"
      @RightBarService.getContents().then(
            (data) =>
                @$log.debug "Promise returned #{data} contents"
                @contents = data
            ,
            (error) =>
                @$log.error "Unable to get contents: #{error}"
            )

    navigateTo: (viewState) ->
      @$log.debug "RightBarCtrl.navigateTo(#{viewState})"
      @$state.go(viewState)

controllersModule.controller('RightBarCtrl', RightBarCtrl)