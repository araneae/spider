
class CompanyViewCtrl

    constructor: (@$log, @$scope, @$state, @CompanyService, @ErrorService, @$location) ->
        @$log.debug "constructing CompanyViewCtrl"
        @company = {}
        
        # fetch data from server
        @loadCompany()

    loadCompany: () ->
        @$log.debug "CompanyViewCtrl.loadCompany()"
        @CompanyService.getCompany().then( 
          (data) =>
            @company = data if data
            @$log.debug "server returned #{data} Company"
          ,
          (error) =>
            @ErrorService.error
            @$log.error "Unable to fetch company settings: #{error}"
        )

    cancel: () ->
        @$log.debug "CompanyViewCtrl.cancel()"
        @$state.go("index")
    
    goToEdit: () ->
        @$log.debug "CompanyViewCtrl.goToEdit()"
        @$state.go("companyEdit")

    goToManageUsers: () ->
        @$log.debug "CompanyViewCtrl.goToManageUsers()"
        @$state.go("companyUsers")
    
controllersModule.controller('CompanyViewCtrl', CompanyViewCtrl)