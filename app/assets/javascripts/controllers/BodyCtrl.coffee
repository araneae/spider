
class BodyCtrl

    constructor: (@$log, @$scope, @$state, @$window, @MenuBarService, @$location) ->
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
        @window = angular.element($window)
        @gotoTopButton = angular.element('#idScrollToTop')
        
        if (@window)
          @window.bind('scroll', () =>
                      if (@window.scrollTop() > 100)
                          @gotoTopButton.fadeIn() if @gotoTopButton
                       else
                          @gotoTopButton.fadeOut() if @gotoTopButton
            )
        
    goToTop: () ->
        @$log.debug "BodyCtrl.goToTop()"
        body = angular.element('html, body')
        angular.element('html, body').animate({scrollTop : 0}, 800) if body

controllersModule.controller('BodyCtrl', BodyCtrl)