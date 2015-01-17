# Service with utility methods

class UtilityService

    @headers = {'Accept': 'application/json', 'Content-Type': 'application/json'}
    @defaultConfig = { headers: @headers }

    constructor: (@$log, @$upload, @$state, @$previousState) ->
        @$log.debug "constructing UtilityService"

    findByProperty: (list, prop, value) ->
       for obj in list
           return obj if obj[prop] is value
       return

    findIndexByProperty: (list, prop, value) ->
       index = -1
       for obj in list
           index = index + 1
           return index if obj[prop] is value
       index

    isStringEmpty: (obj) ->
      if (obj)
        obj?.length is 0
      else
        true

    isEmpty: (obj) ->
      empty = true
      if (obj)
        for prop, value of obj
          empty = false
      empty
  
    isArrayEmpty: (obj) ->
      empty = true
      if (obj)
          empty = obj.length is 0
      empty

    isNumber: (obj) ->
      !isNaN(parseFloat(obj)) && isFinite(obj)

    formatDate: (date) ->
      dateString = null
      if (date)
        year = date.getFullYear()
        month = date.getMonth() + 1
        day = date.getDate()
        dateString = year + "-" + month + "-" + day
      dateString
  
    addDays: (date, days) ->
      newDate = date
      if (date)
        daysInMillis = days * (1000 * 60 * 60 * 24)
        newDate = new Date()
        newDate.setTime(date.getTime() + daysInMillis)
      newDate

    getFileName: (fileName) ->
      index = fileName.lastIndexOf('.')
      if (index > 0) 
        fileName.substring(0, index)
      else
        fileName
    
    uploadFile: (targetUrl, contentType, selectedFile, successFn, errFn, progressFn) ->
        @$log.debug "DatabaseService.upload(#{selectedFile})"
        @$upload.upload({
            url: targetUrl,
            method: 'POST',
            #headers: {'Content-type': contentType},
            #withCredentials: true,
            #data: {data: selectedFile},
            file: selectedFile,
            # set the file formData name ('Content-Desposition'). Default is 'file'
            # fileFormDataName: myFile, //or a list of names for multiple files (html5).
            # customize how data is added to formData. See #40#issuecomment-28612000 for sample code 
            # formDataAppender: function(formData, key, val){}
        })
        .progress( (evt) =>
           @$log.debug "percent: #{parseInt(100.0 * evt.loaded / evt.total)}"
           progressFn(evt) if progressFn
        )
        .success( (data, status, headers, config) =>
            # file is uploaded successfully
            @$log.debug "#{data}"
            successFn(data, status, headers, config) if successFn
        )
        .error( (err) =>
              @$log.error "error in uploading file #{err}"
              errFn(err) if errFn
        )
        #.then(success, error, progress); 
        #.xhr(function(xhr){xhr.upload.addEventListener(...)})// access and attach any event listener to XMLHttpRequest.

    goBack: (defaultState) ->
        previous = @$previousState.get()
        @$log.debug "previous : #{angular.toJson(previous)}"
        if (@isStringEmpty(previous.state.name))
          @$state.go(defaultState)
        else
          @$previousState.go()
          #@$previousState.forget(previous.state.name)

servicesModule.service('UtilityService', UtilityService)
