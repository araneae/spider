# Directive for selecting a year
#
# Usage example: <select-year ng-model="myVar"></select-year>
#

class SelectYearDirective
  
  constructor: (@$log, @$rootScope) ->
    @$log.debug "constructing SelectYearDirective"

  restrict: 'E'
  
  require: '^ngModel'
  
  replace: true
  
  templateUrl: '/assets/partials/selectYear.html'

  link: (scope, element, attrs) =>
    modelAttr = attrs.ngModel
    

directivesModule.directive('selectYear', ['$log', '$rootScope', 
                                                  ($log, $rootScope) ->
                                                    new SelectYearDirective($log, $rootScope)
                                        ])
