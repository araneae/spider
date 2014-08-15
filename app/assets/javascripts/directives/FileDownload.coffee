
class DownloadDirective
  
  constructor: (window) ->
    @URL = window.webkitURL or window.URL
    @Blob = window.Blob
    
  restrict: 'A'
    
  link: (scope, element, attrs) =>
    a = element[0]
    
    if a.draggable
      element.bind "dragstart", (event) ->
        event.originalEvent.dataTransfer.setData "DownloadURL", a.dataset.downloadurl 
    
    a.href = '#'
    element.click -> no if a.href == '#'
      
    attrNames = ['fileDownload', 'mimeType', 'url', 'content']
      
    updateDom = =>
      [filename, mimeType, url, content] = (scope.$eval attrs[attr] for attr in attrNames)
      filename or= 'file'
      mimeType or= 'text/plain'
      
      if url?
        @setupUrlDownload a, url, filename, mimeType
      else if content?
        @setupDataDownload a, content, filename, mimeType
    
    watchers = {}
    for attr in attrNames
      do (attr) ->
        attrs.$observe attr, (attrValue) ->
          watchers[attr]() if watchers[attr]? 
          watchers[attr] = scope.$watch attrValue, updateDom if attrValue
      
  setupUrlDownload: (a, url, filename, mimeType) =>
    @URL.revokeObjectURL a.href if a.href != '#'    
    a.download = filename
    a.href = url
    a.dataset.downloadurl = [mimeType, a.download, a.href].join(":")
    
  setupDataDownload: (a, data, filename, mimeType) =>
    blob = new @Blob [data], type: mimeType
    url = @URL.createObjectURL blob
    @setupUrlDownload a, url, filename, mimeType

directivesModule.directive('fileDownload', ['$window', ($window) ->
                                              new DownloadDirective($window)
                                          ])
