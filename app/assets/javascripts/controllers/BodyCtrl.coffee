
class BodyCtrl

    constructor: (@$log, @$scope, @$state, @MenuBarService, @$location) ->
        @$log.debug "constructing BodyCtrl"
        
        @bodyStyle = {
           background: "url(/assets/images/california-mountain.jpg) no-repeat center center fixed",
           '-webkit-background-size': 'cover',
           '-moz-background-size': 'cover',
           '-o-background-size': 'cover',
           'background-size': 'cover'
        }
        
        # fetch data from server

controllersModule.controller('BodyCtrl', BodyCtrl)