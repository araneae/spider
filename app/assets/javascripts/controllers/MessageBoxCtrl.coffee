
class MessageBoxCtrl

    constructor: (@$log, @$q, @MessageBox, @$scope, @$state, @$stateParams, @$location, @ErrorService) ->
        @$log.debug "constructing MessageBoxCtrl"
        @messageBoxes = []
        @removedIds=[]
        # load data from server
        @listMessageBoxes()

    listMessageBoxes: () ->
        @$log.debug "MessageCtrl.listMessageBoxes()"
        @MessageBox.query().$promise.then(
            (data) =>
                @$log.debug "Promise returned #{data.length} messages"
                @messageBoxes = []
                for box in data
                   box.messageCount = 1
                   @messageBoxes.push(box) if box.messageBoxType == 'CUSTOM'
                # sort the message boxes
                @messageBoxes.sort((a, b) =>
                                        a.name > b.name
                )
            ,
            (error) =>
                @$log.error "Unable to get messages: #{error}"
                @ErrorService.error("Unable to fetch message box information from server!")
            )

    remove: (messageBoxId) ->
        @$log.debug "MessageCtrl.remove(#{id})"
        @removedIds.push(id)
        index = @UtilityService.findIndexByProperty(@messageBoxes, 'messageBoxId', messageBoxId)
        @messageBoxes.splice(index, 1)

    save: () ->
        @$log.debug "MessageCtrl.save()"
        promises = []
        for id in @removedIds
            promise = @UserTag.remove({messageBoxId: id})
            promises.push(promise).$promise
        
        # TBD: only update the dirty ones
        for box in @messageBoxes
            promise = @MessageBox.update(box).$promise
            promises.push(promise)

        # wait for all promises to complete
        @$q.all(promises).then(
            () =>
                 @$log.debug "Successfully updated"
                 @ErrorService.success("Successfully updated label!")
                 @$state.go("messages.messageList")
           ,
           () =>
                @$log.debug "one of the promise failed"
                @ErrorService.error("Unable to update message box!")
        )    

    cancel: () ->
        @$log.debug "MessageBoxCtrl.cancel()"
        #@$state.go('^', @$stateParams)
        @$state.go('messages.messageList', @$stateParams)

controllersModule.controller('MessageBoxCtrl', MessageBoxCtrl)