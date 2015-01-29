# Directive for controlling access by user permissions
#
# Usage example: <div has-permission="<permission>">
#                  other contents
#                </div>
#

class HasPermissionDirective
  
  constructor: (@$log, @UserPermissionService) ->
    @$log.debug "constructing HasPermissionDirective"
    
  restrict: 'A'
    
  link: (scope, element, attrs) =>
    if (!angular.isString(attrs.hasPermission))
        @$log.debug "hasPermission value #{attrs.hasPermission} must be a string"
        throw "hasPermission value must be a string"
    
    value = attrs.hasPermission.trim()
    notPermissionFlag = (value[0] is '!')
    
    if (notPermissionFlag)
      value = value.slice(1).trim()

    toggleVisibilityBasedOnPermission = () =>
      hasPermission = @UserPermissionService.hasPermission(value)
 
      if ( (hasPermission and !notPermissionFlag) or 
           (!hasPermission and notPermissionFlag))
          element.show()
      else
          element.hide()
      
    toggleVisibilityBasedOnPermission()
    scope.$on('permissionsChanged', toggleVisibilityBasedOnPermission)

directivesModule.directive('hasPermission', ['$log', 'UserPermissionService', ($log, UserPermissionService) ->
                                              new HasPermissionDirective($log, UserPermissionService)
                                          ])
