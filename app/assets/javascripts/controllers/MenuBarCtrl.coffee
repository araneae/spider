
class MenuBarCtrl

    constructor: (@$log, @$rootScope, @$state, @MenuBarService, @$location) ->
        @$log.debug "constructing MenuBarCtrl"
        @contents
        @currentPath = @$location.path()
        
        # fetch data from server
        @loadContents()

    loadContents: () ->
      @$log.debug "RightBarCtrl.loadContents()"
      @MenuBarService.getContents().then(
            (data) =>
                @$log.debug "Promise returned #{data} contents"
                @contents = data
            ,
            (error) =>
                @$log.error "Unable to get contents: #{error}"
            )
  
    isMenuActive: (path) ->
      @$log.debug "RightBarCtrl.isMenuActive(#{@currentPath})"
      pos = @currentPath.indexOf(path)
      pos is 0 
  
controllersModule.controller('MenuBarCtrl', MenuBarCtrl)