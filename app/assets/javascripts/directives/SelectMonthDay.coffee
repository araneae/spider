# Directive for selecting a day of month
#
# Usage example: <select-month-day ng-model="myVar"></select-month-day>
#

class SelectMonthDayDirective
  
  constructor: (@$log, @$rootScope) ->
    @$log.debug "constructing SelectMonthDayDirective"

  restrict: 'E'
  
  require: '^ngModel'
  
  replace: true
  
  templateUrl: '/assets/partials/selectMonthDay.html'

  link: (scope, element, attrs) =>
    modelAttr = attrs.ngModel
    

directivesModule.directive('selectMonthDay', ['$log', '$rootScope', 
                                                  ($log, $rootScope) ->
                                                    new SelectMonthDayDirective($log, $rootScope)
                                        ])
