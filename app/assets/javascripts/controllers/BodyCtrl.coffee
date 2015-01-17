
class BodyCtrl

    constructor: (@$log, @$scope, @$state, @MenuBarService, @$location) ->
        @$log.debug "constructing BodyCtrl"
        
        # removing the background image for now
        # background: "url(/assets/images/california-mountain.jpg) no-repeat center center fixed",
        @bodyStyle = {
           background: 'rgb(242, 234, 221)',
           '-webkit-background-size': 'cover',
           '-moz-background-size': 'cover',
           '-o-background-size': 'cover',
           'background-size': 'cover'
        }
        
        # fetch data from server

controllersModule.controller('BodyCtrl', BodyCtrl)