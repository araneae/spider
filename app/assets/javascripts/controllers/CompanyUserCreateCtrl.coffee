
class CompanyUserCreateCtrl

    constructor: (@$log, @$scope, @$state, @CompanyService, @CompanyUserService, @CompanyUser, @ErrorService, @EnumService, @$location) ->
        @$log.debug "constructing CompanyUserCreateCtrl"
        @company = {}
        @companyUser = {}
        
        # fetch data from server
        @loadCompany()

    loadCompany: () ->
        @$log.debug "CompanyUserCreateCtrl.loadCompany()"
        @CompanyService.getCompany().then( 
          (data) =>
            @company = data if data
            @$log.debug "server returned #{data} Company"
          ,
          (error) =>
            @ErrorService.error
            @$log.error "Unable to fetch company settings: #{error}"
        )

    save: () ->
        @$log.debug "CompanyUserCreateCtrl.save()"
        @companyUser.companyId =  @company.companyId if @company
        @CompanyUser.save(@companyUser).$promise.then( 
          (data) =>
            @$log.debug "server returned #{data} Company"
            @ErrorService.success(data.message)
            @$state.go("companyUsers")
          ,
          (error) =>
            @ErrorService.error("Oops, something wrong! Unable to create #{@company.name}")
            @$log.error "Unable to create company: #{error}"
        )

    cancel: () ->
        @$log.debug "CompanyUserCreateCtrl.cancel()"
        @$state.go("companyUsers")

controllersModule.controller('CompanyUserCreateCtrl', CompanyUserCreateCtrl)