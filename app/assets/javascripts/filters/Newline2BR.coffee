
filtersModule.filter('Newline2BR', () ->
                                (text) =>
                                  return text.replace(/\n/g, '<br/>') if text
                                  ''
                    )
