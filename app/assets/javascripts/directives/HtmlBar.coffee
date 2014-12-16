# Directive for rendering html contents
#
# Usage example: <html-bar ng-model="myMessageVar"></html-bar>
#
class HtmlBarDirective
  
  constructor: (@$compile, @$parse, @$log) ->
    @$log.debug "constructing HtmlBarDirective"

  restrict: 'E'

  link: (scope, element, attrs) =>
    modelAttr = attrs.ngModel
    scope.$watch(modelAttr, () =>
                    element.html(@$parse(modelAttr)(scope))
                    @$compile(element.contents())(scope)
                )

directivesModule.directive('htmlBar', ['$compile', '$parse', '$log', ($compile, $parse, $log) ->
                                          new HtmlBarDirective($compile, $parse, $log)
                                        ])

