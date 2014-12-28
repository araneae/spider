
class FolderCreateCtrl

    constructor: (@$log, @DocumentFolder, @$scope, @$state, @$stateParams, @$location, @ErrorService) ->
        @$log.debug "constructing FolderCreateCtrl"
        @folder = {default: false, shared: false}

    create: () ->
        @$log.debug "FolderCreateCtrl.create()" 
        @DocumentFolder.save(@folder).$promise.then( 
            (data) =>
              @$log.debug "server returned #{data} DocumentFolder"
              @$state.go('folder.documents', @$stateParams, {reload: true})
            ,
            (error) =>
              @ErrorService.error("Unable to create folder!")
              @$log.debug "Unable to create folder #{error}"
        )

    cancel: () ->
        @$log.debug "FolderCreateCtrl.cancel()"
        #@$state.go('^', @$stateParams)
        @$state.go('folder.documents', @$stateParams)

controllersModule.controller('FolderCreateCtrl', FolderCreateCtrl)