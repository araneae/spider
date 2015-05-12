
class BodyCtrl

    constructor: (@$log, @$scope, @$rootScope, @$state, @$window, @User,
                          @MenuBarService, @UserPermissionService, @ErrorService, @$location) ->
        @$log.debug "constructing BodyCtrl"
        @userPermissions = []
        
        # removing the background image for now
        # background: "url(/assets/images/california-mountain.jpg) no-repeat center center fixed",
        @bodyStyle = {
           background: 'rgb(242, 234, 221)',
           '-webkit-background-size': 'cover',
           '-moz-background-size': 'cover',
           '-o-background-size': 'cover',
           'background-size': 'cover'
        }
        @user = {}
        @message
        
        @$rootScope.$on('$stateChangeSuccess', (event, toState, toParams, fromState, fromParams) =>
            permission = toState.permission
            if (permission)
              name = permission.name
              if (angular.isString(name) and !@UserPermissionService.hasPermission(name))
                  @ErrorService.error("Not authorized to access!")
                  @$state.go(fromState.name, toParams)
          )
        
        @window = angular.element($window)
        @gotoTopButton = angular.element('#idScrollToTop')
        
        if (@window)
          @window.bind('scroll', () =>
                      if (@window.scrollTop() > 100)
                          @gotoTopButton.fadeIn() if @gotoTopButton
                       else
                          @gotoTopButton.fadeOut() if @gotoTopButton
            )

        @loadUser()

        # fetch data from server
        @UserPermissionService.initUserPermissions()

    loadUser: () ->
        @$log.debug "BodyCtrl.loadUser()"
        @User.get().$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data} user"
                @user = data
                @message = "Hello, #{data.firstName}"
            ,
            (error) =>
                @ErrorService.error("Oops, something wrong! Unable to fetch data from server.")
                @$log.error "Unable to get user: #{error}"
            )

    goToTop: () ->
        @$log.debug "BodyCtrl.goToTop()"
        body = angular.element('html, body')
        angular.element('html, body').animate({scrollTop : 0}, 800) if body

controllersModule.controller('BodyCtrl', BodyCtrl)