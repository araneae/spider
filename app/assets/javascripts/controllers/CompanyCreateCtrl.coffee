
class CompanyCreateCtrl

    constructor: (@$log, @$scope, @$state, @CompanyService, @ErrorService, @$location) ->
        @$log.debug "constructing CompanyCreateCtrl"
        @company = {}
        
        # google places options
        @placesOptions = {
                country: '',
                types: 'geocode'
        }
        
        # fetch data from server

    save: () ->
        @$log.debug "CompanyCreateCtrl.save()"
        @company.status='ACTIVE'
        @CompanyService.save(@company).then( 
          (data) =>
            @$log.debug "server returned #{data} Company"
            @ErrorService.success(data.message)
            @$state.go("index")
          ,
          (error) =>
            @ErrorService.error("Oops, something wrong! Unable to create #{@company.name}")
            @$log.error "Unable to create company: #{error}"
        )

    cancel: () ->
        @$log.debug "CompanyCreateCtrl.cancel()"
        @$state.go("index")

controllersModule.controller('CompanyCreateCtrl', CompanyCreateCtrl)