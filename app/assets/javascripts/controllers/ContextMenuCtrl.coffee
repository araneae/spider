
class ContextMenuCtrl

    constructor: (@$log, @$rootScope, @$state) ->
        @$log.debug "constructing ContextMenuCtrl"
        @searchText

    onClickMenuItem: (menuItem) ->
        @$log.debug "ContextMenuCtrl.onClickMenuItem(#{menuItem})"
        @$rootScope.$broadcast('contextMenu', {menuItem: menuItem})

controllersModule.controller('ContextMenuCtrl', ContextMenuCtrl)