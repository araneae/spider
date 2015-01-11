# Directive for rendering html contents
#
# Usage example: <input type="text" ng-model="myLocationVar" google-places>
#
class GooglePlacesDirective
  
  constructor: (@$compile, @$parse, @$log) ->
    @$log.debug "constructing GooglePlacesDirective"

  require: '?ngModel'

  restrict: 'A'
  
  scope: {
      ngModel: '=',
      details: '=?',
      options: '='
  } 

  link: (scope, element, attrs) =>
    options = {
          types: [],
          componentRestrictions: {}
    }
    
    # options for autocomplete
    options

    # convert options provided to options
    initOpts = () =>
        options = {}
        if (scope.options)
          if (scope.options.types)
            options.types = []
            options.types.push(scope.options.types)

          if (scope.options.bounds)
            options.bounds = scope.options.bounds

          if (scope.options.country)
            options.componentRestrictions = {
                          country: scope.options.country
            }

    initOpts()

    # create new autocomplete
    # reinitializes on every change of the options provided
    newAutocomplete = () =>
          scope.gPlace = new google.maps.places.Autocomplete(element[0], options);
          google.maps.event.addListener(scope.gPlace, 'place_changed', () =>
                                scope.$apply( () =>
                                  #scope.details = scope.gPlace.getPlace()
                                  place = scope.gPlace.getPlace()
                                  if (place)
                                    scope.details = { 
                                                    lat: place.geometry.location.lat(),
                                                    lng: place.geometry.location.lng()
                                                  }
                                    #scope.ngModel = element.val()
                                    scope.ngModel = place.formatted_address
                                )
          )

    newAutocomplete()
        
    #scope.gPlace = new google.maps.places.Autocomplete(element[0], options)
 
    # watch options provided to directive
    scope.watchOptions = () =>
          scope.options

    scope.$watch(scope.watchOptions, () =>
                    initOpts()
                    newAutocomplete()
                    element[0].value = ''
                    scope.ngModel = element.val();
                , 
                true)

directivesModule.directive('googlePlaces', ['$compile', '$parse', '$log', ($compile, $parse, $log) ->
                                          new GooglePlacesDirective($compile, $parse, $log)
                                        ])

