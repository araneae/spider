# Directive for showing error message
#
# Usage example: <div alert-bar message="myMessageVar"></div>
#

class AlertBarDirective
  
  constructor: (@$parse, @$log, @$timeout) ->

  restrict: 'A'
  
#  template: '<div class="alert alert-error alert-bar"' +
#            'ng-show="alertMessage">' +
#            '<button type="button" class="close" ng-click="hideAlert()">x</button>' +
#            '{{alertMessage}}</div>'
  templateUrl: '/assets/partials/alertBar.html'

  link: (scope, element, attrs) =>
    alertMessageAttr = attrs['message']
    alertTypeAttr = attrs['type']
    scope.alertMessage
    scope.alertMessageTimer
    scope.alertClass
    scope.$watch(alertMessageAttr, (newVal) =>
                            scope.alertMessage = newVal
                            @$timeout.cancel(scope.alertMessageTimer) if scope.alertMessageTimer
                            scope.alertMessageTimer = @$timeout(scope.hideAlert, 5000)
                )
    scope.$watch(alertTypeAttr, (newVal) =>
                            scope.alertClass = @getStyle(newVal)
                )
    scope.hideAlert = () =>
      @$log.debug "scope.hideAlert"
      scope.alertMessage
      scope.alertClass
      @$timeout.cancel(scope.alertMessageTimer) if scope.alertMessageTimer
      scope.alertMessageTimer
      # Also clear the error message on the bound variable.
      # Do this so that if the same error happens again
      # the alert bar will be shown again next time.
      @$parse(alertMessageAttr).assign(scope, null)
      @$parse(alertTypeAttr).assign(scope, null)
      
   getStyle: (alertTypeAttr) ->
      return "alert alert-warning alert-dismissible" if alertTypeAttr is 'error'
      return "alert alert-success alert-dismissible" if alertTypeAttr is 'success'
      "alert alert-success alert-dismissible"

directivesModule.directive('alertBar', ['$parse', '$log', '$timeout', ($parse, $log, $timeout) ->
                                          new AlertBarDirective($parse, $log, $timeout)
                                        ])
