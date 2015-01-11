
class CompanyEditCtrl

    constructor: (@$log, @$scope, @$state, @CompanyService, @ErrorService, @$location) ->
        @$log.debug "constructing CompanyEditCtrl"
        @company = {}
        
        # google places options
        @placesOptions = {
                country: '',
                types: 'geocode'
        }
        
        # fetch data from server
        @loadCompany()

    loadCompany: () ->
        @$log.debug "CompanyEditCtrl.loadCompany()"
        @CompanyService.getCompany().then( 
          (data) =>
            @company = data if data
            @company.status = "ACTIVE" if !data.status
            @$log.debug "server returned #{data} Company"
          ,
          (error) =>
            @ErrorService.error
            @$log.error "Unable to fetch company settings: #{error}"
        )

    save: () ->
        @$log.debug "CompanyEditCtrl.save()"
        @CompanyService.save(@company).then( 
          (data) =>
            @$log.debug "server returned #{data} Company"
            @ErrorService.success(data.message)
            @$state.go("companyView")
          ,
          (error) =>
            @ErrorService.error("Oops, something wrong! Unable to create #{@company.name}")
            @$log.error "Unable to create company: #{error}"
        )

    cancel: () ->
        @$log.debug "CompanyEditCtrl.cancel()"
        @$state.go("companyView")

controllersModule.controller('CompanyEditCtrl', CompanyEditCtrl)