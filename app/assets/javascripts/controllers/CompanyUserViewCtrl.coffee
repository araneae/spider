
class CompanyUserViewCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @CompanyService, @CompanyUserService, @CompanyUser, @ErrorService, @EnumService, @$location) ->
        @$log.debug "constructing CompanyUserViewCtrl"
        @companyUserId = parseInt(@$stateParams.companyUserId)
        @companyUser = {}
        
        # fetch data from server
        @loadCompanyUser(@companyUserId)

    loadCompanyUser: (companyUserId) ->
        @$log.debug "CompanyUserViewCtrl.loadCompanyUser(companyUserId)"
        @CompanyUser.get({companyUserId: companyUserId}).$promise.then( 
          (data) =>
            @companyUser = data if data
            @$log.debug "server returned #{data} Company"
          ,
          (error) =>
            @ErrorService.error
            @$log.error "Unable to fetch company settings: #{error}"
        )

    cancel: () ->
        @$log.debug "CompanyUserViewCtrl.cancel()"
        @$state.go("companyUsers")

    goToEdit: () ->
        @$log.debug "CompanyUserViewCtrl.goToEdit()"
        @$state.go("companyUserEdit", {companyUserId: @companyUser.companyUserId})

controllersModule.controller('CompanyUserViewCtrl', CompanyUserViewCtrl)