# Directive for selecting a month
#
# Usage example: <select-month ng-model="myVar"></select-month>
#

class SelectMonthDirective
  
  constructor: (@$log, @$rootScope) ->
    @$log.debug "constructing SelectMonthDirective"

  restrict: 'E'
  
  require: '^ngModel'
  
  replace: true
  
  templateUrl: '/assets/partials/selectMonth.html'

  link: (scope, element, attrs) =>
    modelAttr = attrs.ngModel
    

directivesModule.directive('selectMonth', ['$log', '$rootScope', 
                                                  ($log, $rootScope) ->
                                                    new SelectMonthDirective($log, $rootScope)
                                        ])
