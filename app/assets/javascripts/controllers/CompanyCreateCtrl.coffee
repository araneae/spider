
class CompanyCreateCtrl

    constructor: (@$log, @$scope, @$state, @CompanyService, @ErrorService, @$location) ->
        @$log.debug "constructing CompanyCreateCtrl"
        @company = {}
        
        # fetch data from server
        @loadCompany()

    loadCompany: () ->
        @$log.debug "CompanyCreateCtrl.loadCompany()"
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
        @$log.debug "CompanyCreateCtrl.save()"
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