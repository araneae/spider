
class IndustryCtrl

    constructor: (@$log, @IndustryService) ->
        @$log.debug "constructing IndustryController"
        @industry = {}
        @industries = []
        @editMode = false
        @listIndustries()
        
    listIndustries: () ->
        @$log.debug "IndustryCtrl.listIndustries()"
        @editMode = false
        @industry = {}
        @IndustryService.listIndustries()
        .then(
            (data) =>
                @$log.debug "Promise returned #{data.length} Industries"
                @industries = data
            ,
            (error) =>
                @$log.error "Unable to get Industries: #{error}"
            )
    
    createIndustry: () ->
        @$log.debug "IndustryCtrl.createIndustry()"
        @editMode = false
        @IndustryService.createIndustry(@industry)
        .then( 
            (data) =>
              @$log.debug "Promise returned #{data} Industry"
              @listIndustries()
            ,
            (error) =>
                @$log.error "Unable to create Industry: #{error}"
            )

    updateIndustry: () ->
        @$log.debug "IndustryCtrl.updateIndustry()"
        @editMode = false
        @IndustryService.updateIndustry(@industry)
        .then( 
            (data) =>
              @$log.debug "Promise returned #{data} Industry"
              @listIndustries()
            ,
            (error) =>
                @$log.error "Unable to save Industry: #{error}"
            )

    saveIndustry: () ->
        if @editMode
            @updateIndustry()
         else
            @createIndustry()

    cancelIndustry: () ->
        @$log.debug "IndustryCtrl.cancelIndustry()"
        @industry = {}
        @editMode = false
    
    deleteIndustry: (index) ->
        @$log.debug "IndustryCtrl.deleteIndustry(#{index})"
        item = @industries[index]
        @editMode = false
        @IndustryService.deleteIndustry(item.id)
        .then( (data) =>
            @$log.debug "server returned #{data} Industry"
            @listIndustries()
        )
    
    editIndustry: (index) ->
        @$log.debug "IndustryCtrl.editIndustry(#{index})"
        item = @industries[index]
        @industry = item
        @editMode = true
    
    getCreateLabel: () ->
        if @editMode
          'Edit Industry'
        else
           'Create Industry'

controllersModule.controller('IndustryCtrl', IndustryCtrl)