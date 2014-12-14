
class DomainCreateCtrl

    constructor: (@$log, @$scope, @$state, @DomainService, @Domain, @Industry, @ErrorService, @$location) ->
        @$log.debug "constructing DomainCreateCtrl"
        @domain = {}
        @industry = {}
        @industries = []
        # fetch data from server
        @listIndustries()

    listIndustries: () ->
        @$log.debug "DomainCreateCtrl.listIndustries()"
        @Industry.query().$promise
          .then(
             (data) =>
                @$log.debug "Promise returned #{data.length} Industries"
                @industries = data
             ,
             (error) =>
                @ErrorService.error()
                @$log.error "Unable to get Industries: #{error}"
             )
    
    save: () ->
        @$log.debug "DomainCreateCtrl.save()"
        @domain.industryId = @industry.industryId
        @domain.industryName = @industry.name
        domain = new @Domain(@domain)
        domain.$save().then( 
          (data) =>
            @$log.debug "server returned #{data} Domain"
            @$state.go("domains")
          ,
          (error) =>
            @ErrorService.error("Oops, something wrong! Unable to create #{@domain.name}")
            @$log.error "Unable to create Domain: #{error}"
        )

    cancel: () ->
        @$log.debug "DomainCreateCtrl.cancel()"
        @$state.go("domains")

controllersModule.controller('DomainCreateCtrl', DomainCreateCtrl)