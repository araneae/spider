
class CompanyUserEditCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @CompanyService, @CompanyUserService, @CompanyUser, @ErrorService, @EnumService, @$location) ->
        @$log.debug "constructing CompanyUserEditCtrl"
        @companyUserId = parseInt(@$stateParams.companyUserId)
        @companyUser = {}
        
        # fetch data from server
        @loadCompanyUser(@companyUserId)

    loadCompanyUser: (companyUserId) ->
        @$log.debug "CompanyUserEditCtrl.loadCompanyUser(companyUserId)"
        @CompanyUser.get({companyUserId: companyUserId}).$promise.then( 
          (data) =>
            @companyUser = data if data
            @$log.debug "server returned #{data} Company"
          ,
          (error) =>
            @ErrorService.error
            @$log.error "Unable to fetch company settings: #{error}"
        )

    save: () ->
        @$log.debug "CompanyUserEditCtrl.save()"
        @CompanyUser.update(@companyUser).$promise.then( 
          (data) =>
            @$log.debug "server returned #{data} Company"
            @ErrorService.success(data.message)
            @$state.go("companyUsers")
          ,
          (error) =>
            @ErrorService.error("Oops, something wrong! Unable to update company user.")
            @$log.error "Unable to update company user: #{error}"
        )

    cancel: () ->
        @$log.debug "CompanyUserEditCtrl.cancel()"
        @$state.go("companyUsers")

controllersModule.controller('CompanyUserEditCtrl', CompanyUserEditCtrl)