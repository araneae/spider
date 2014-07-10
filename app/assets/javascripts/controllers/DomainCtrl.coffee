
class DomainCtrl

    constructor: (@$log, @DomainService, @Domain, @IndustryService, @UtilityService, @$location) ->
        @$log.debug "constructing DomainController"
        @domain = {}
        @domains = []
        @editMode = false
        @industry = {}
        @industries = []
        # fetch data from server
        @listIndustries()
        @listDomains()

    listIndustries: () ->
        @$log.debug "DomainCtrl.listIndustries()"
        @editMode = false
        @IndustryService.listIndustries()
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
        @editMode = false
        @domain = {}
        @industry = {}
        @domains = @Domain.query()
    
    create: () ->
        @$log.debug "DomainCtrl.createDomain()"
        @editMode = false
        @domain.industryId = @industry.id
        domain = new @Domain(@domain);
        domain.$save().then( (data) =>
            @$log.debug "server returned #{data} Domain"
            @listDomains()
        )

    update: () ->
        @$log.debug "DomainCtrl.updateDomain()"
        @editMode = false
        @domain.industryId = @industry.id
        domain = new @Domain(@domain);
        domain.$update().then( (data) =>
            @$log.debug "server returned #{data} Domain"
            @listDomains()
        )

    save: () ->
        if @editMode
            @updateDomain()
         else
            @createDomain()

    cancel: () ->
        @$log.debug "DomainCtrl.cancelDomain()"
        @domain = {}
        @editMode = false

    delete: (index) ->
        @$log.debug "DomainCtrl.deleteDomain(#{index})"
        item = @domains[index]
        @editMode = false
        @Domain.remove({id: item['id']})
        @listDomains()

    edit: (index) ->
        @$log.debug "DomainCtrl.editDomain(#{index})"
        item = @domains[index]
        @domain = item
        @industry = @UtilityService.findByProperty(@industries, 'id', item['industryId'])
        @editMode = true
    
    getCreateLabel: () ->
        if @editMode
          'Edit Domain'
        else
           'Create Domain'

controllersModule.controller('DomainCtrl', DomainCtrl)