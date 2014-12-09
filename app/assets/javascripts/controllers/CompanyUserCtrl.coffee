
class CompanyUserCtrl

    constructor: (@$log, @$scope, @$state, @$stateParams, @CompanyService, @CompanyUserService, @CompanyUser, @UtilityService, @ErrorService) ->
        @$log.debug "constructing CompanyUserCtrl"
        @company
        @companyUsers = []
        @removeObj = {}
        @removeAlert = false
        @$scope.$on('contextMenu', (event, data) =>
                                    @$log.debug "received message contextMenu(#{data.menuItem})"
                                    @refresh() if data.menuItem is "refresh"
                                    @goToAddUser() if data.menuItem is "addUser"
        )
        
        # load list of companyUsers from server
        @loadCompany()
        @listCompanyUsers()
    
    loadCompany: () ->
        @$log.debug "CompanyUserCtrl.loadCompany()"
        @CompanyService.getCompany().then( 
          (data) =>
            @company = data
            @$log.debug "server returned #{data} Company"
          ,
          (error) =>
            @ErrorService.error
            @$log.error "Unable to fetch company settings: #{error}"
        )
        
    listCompanyUsers: () ->
        @$log.debug "CompanyUserCtrl.listCompanyUsers()"
        @CompanyUser.query().$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} companyUsers"
                @companyUsers = data
            ,
            (error) =>
                @ErrorService.error
                @$log.error "Unable to get companyUsers: #{error}"
            )
        
    refresh: () ->
      @listCompanyUsers()
    
    showRemoveAlert: (companyUser) ->
        @$log.debug "CompanyUserCtrl.showRemoveAlert(#{companyUser})"
        @removeObj = companyUser
        @removeAlert = true
    
    cancelRemove: () ->
        @$log.debug "CompanyUserCtrl.cancelRemove()"
        @removeAlert = false
        @removeObj={}

    remove: () ->
        @$log.debug "CompanyUserCtrl.remove()"
        @removeAlert = false
        @CompanyUser.remove({companyUserId: @removeObj.companyUserId}).$promise.then(
            (data) =>
                @$log.debug "Successfully deleted companyUser!"
                @removeObj = {}
                @listCompanyUsers()
                @ErrorService.success(data.message)
            ,
            (error) =>
                @$log.error "Unable to delete companyUser: #{error}!"
            )

    goToCompanyUserView: (companyUser) ->
        @$log.debug "CompanyUserCtrl.goToCompanyUserView(#{companyUser})"
        @$state.go("companyUserView", {companyUserId: companyUser.companyUserId})

    goToAddUser: () ->
        @$log.debug "CompanyUserCtrl.goToAddUser()"
        @$state.go("companyUserCreate")
        
controllersModule.controller('CompanyUserCtrl', CompanyUserCtrl)