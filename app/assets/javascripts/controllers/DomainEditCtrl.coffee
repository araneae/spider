
class DomainEditCtrl

    constructor: (@$log, @$state, @$stateParams, @Domain, @ErrorService, @$location) ->
        @$log.debug "constructing DomainController"
        @domainId = parseInt(@$stateParams.domainId)
        @industry = {}
        @industries = []
        @domain = {}
        # fetch data from server
        @loadDomain()

    loadDomain: () ->
        @$log.debug "DomainEditCtrl.loadDomain()"
        @domain = {}
        @Domain.get({domainId: @domainId}).$promise.then(
              (data) =>
                @$log.debug "Promise returned #{data.length} domains"
                @domain = data
                @industry = {name: @domain.industryName}
                @industries.push(@industry)
              ,
              (error) =>
                @ErrorService.error()
                @$log.error "Unable to fetch data from server: #{error}"
          )
    
    save: () ->
        @$log.debug "DomainEditCtrl.save()"
        domain = new @Domain(@domain);
        domain.$update().then( 
              (data) =>
                @ErrorService.success("Successfully update domain #{@domain.name}")
                @$log.debug "server returned #{data} Domain"
                @$state.go("domain")
             (error) =>
                @ErrorService.error("Oops, something wrong! Unable to update #{@domain.name}!")
                @$log.error "Unable to get Industries: #{error}"
        )

    cancel: () ->
        @$log.debug "DomainEditCtrl.cancel()"
        @$state.go("domain")

controllersModule.controller('DomainEditCtrl', DomainEditCtrl)