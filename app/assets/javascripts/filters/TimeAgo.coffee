
filtersModule.filter("timeago", () ->
          (time, local, raw) =>
            if (!time) 
              return "never"
 
            if (!local)
               local = Date.now()
 
            if (angular.isDate(time))
                time = time.getTime()
            else if (typeof(time) is "string")
                time = new Date(time).getTime()
            
            if (angular.isDate(local))
                local = local.getTime()
             else if (typeof(local) is "string")
                local = new Date(local).getTime()
 
            if (typeof(time) isnt 'number' or typeof(local) isnt 'number')
                return
            
            offset = Math.abs((local - time) / 1000)
            span = []
            MINUTE = 60
            HOUR = 3600
            DAY = 86400
            WEEK = 604800
            MONTH = 2629744
            YEAR = 31556926
            DECADE = 315569260
            
            if (offset <= MINUTE)
              span = [ '', raw ? 'Now' : 'Less than a minute' ]
            else if (offset < (MINUTE * 60))
              span = [ Math.round(Math.abs(offset / MINUTE)), 'Min' ]
            else if (offset < (HOUR * 24))
              span = [ Math.round(Math.abs(offset / HOUR)), 'Hour' ]
            else if (offset < (DAY * 7))
              span = [ Math.round(Math.abs(offset / DAY)), 'Day' ]
            else if (offset < (WEEK * 52))
              span = [ Math.round(Math.abs(offset / WEEK)), 'Week' ]
            else if (offset < (YEAR * 10))
              span = [ Math.round(Math.abs(offset / YEAR)), 'Year' ]
            else if (offset < (DECADE * 100))
              span = [ Math.round(Math.abs(offset / DECADE)), 'Decade' ]
            else
              span = [ '', 'A long time' ]
 
            if (span[0] > 1) 
              span[1] += 's'
              
            span = span.join(' ')
            
            if (raw is true)
                return span
                
            if (time <= local)
              return (span + ' Ago')
              
            'in ' + span
  )