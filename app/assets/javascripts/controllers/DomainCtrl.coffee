
class DomainCtrl

    constructor: (@$log, @$scope, @$state, @DomainService, @Domain, @Industry, @ErrorService, @UtilityService, @$location) ->
        @$log.debug "constructing DomainController"
        @domain = {}
        @domains = []
        @industry = {}
        @industries = []
        @$scope.$on('contextMenu', (event, data) =>
                                    @$log.debug "received message contextMenu(#{data.menuItem})"
                                    @refresh() if data.menuItem is "refresh"
                                    @goToCreate() if data.menuItem is "create"
        )
        # fetch data from server
        @listIndustries()
        @listDomains()

    listIndustries: () ->
        @$log.debug "DomainCtrl.listIndustries()"
        @Industry.query().$promise
          .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Industries"
                @industries = data
            ,
            (error) =>
                @$log.error "Unable to get Industries: #{error}"
            )
    
    listDomains: () ->
        @$log.debug "DomainCtrl.listDomains()"
        @domain = {}
        @industry = {}
        @domains = @Domain.query().$promise
          .then(
            (data) =>
              @$log.debug "Promise returned #{data.length} Domains"
              @domains = data
            ,
            (error) =>
              @ErrorService.error("Opps, something wrong. Unable to fetch data from server!")
              @$log.error "Unable to get fetch Domains: #{error}"
          )

    refresh: () ->
        @listDomains()

    goToCreate: () ->
        @$log.debug "DomainCtrl.goToCreate()"
        @$state.go("domainCreate")
    
    delete: (domain) ->
        @$log.debug "DomainCtrl.delete(#{domain.domainId})"
        @Domain.remove({domainId: domain.domainId})
        @listDomains()
    
    goToEdit: (domain) ->
        @$log.debug "IndustryCtrl.goToEdit(#{domain})"
        @$state.go("domainEdit", {domainId: domain.domainId})
        
controllersModule.controller('DomainCtrl', DomainCtrl)