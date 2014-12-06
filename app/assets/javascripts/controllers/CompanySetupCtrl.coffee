
class CompanySetupCtrl

    constructor: (@$log, @$scope, @$state, @CompanyService, @Company, @Industry, @ErrorService, @$location) ->
        @$log.debug "constructing CompanySetupCtrl"
        @company = {}
        # fetch data from server

    save: () ->
        @$log.debug "CompanySetupCtrl.save()"
        company = new @Company(@company)
        company.$save().then( 
          (data) =>
            @$log.debug "server returned #{data} Company"
            @$state.go("company")
          ,
          (error) =>
            @ErrorService.error("Oops, something wrong! Unable to create #{@company.name}")
            @$log.error "Unable to create Industry: #{error}"
        )

    cancel: () ->
        @$log.debug "CompanySetupCtrl.cancel()"
        @$state.go("company")

controllersModule.controller('CompanySetupCtrl', CompanySetupCtrl)