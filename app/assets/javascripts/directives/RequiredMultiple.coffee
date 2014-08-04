
directivesModule.directive('requiredMultiple', () ->
    {
      restrict: 'EA',
      require: '?ngModel',
      link: (scope, elm, attr, ctrl) =>
        return if (! ctrl) 
        #force truthy in case we are on non input element
        attr.required = true
        validator = (value) =>
            if (attr.required and (isEmpty(value) or value is false)) 
              ctrl.$setValidity('required', false)
              return
            else 
              ctrl.$setValidity('required', true)
              return value

        isEmpty= (value) =>
          angular.isUndefined(value) or 
          (angular.isArray(value) and value.length is 0) or 
          value is '' or
          value is null or 
          value isnt value
        
        ctrl.$formatters.push(validator)
        ctrl.$parsers.unshift(validator)
        attr.$observe('required', () =>
                    validator(ctrl.$viewValue)
        )
    }
  )