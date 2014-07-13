
dependencies = [
#    'ngRoute',
    'ngResource',
    'ui.bootstrap',
    'ui.router',
    'angularFileUpload',
    'myApp.filters',
    'myApp.services',
    'myApp.controllers',
    'myApp.directives',
    'myApp.common',
    'myApp.routeConfig'
]

app = angular.module('myApp', dependencies)

angular.module('myApp.routeConfig', ['ui.router'])
    .config ($stateProvider, $urlRouterProvider) ->
       $urlRouterProvider.otherwise('/');
       $stateProvider
          .state('industry', {
              url: '/industry',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/industry.html'
                }
              }
          })
          .state('skill', {
              url: '/skill',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/skill.html'
                }
              }
          })
          .state('domain', {
              url: '/domain',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/domain.html'
                }
              }
          })
          .state('userSkillCreate', {
              url: '/userSkillCreate',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/userSkillCreate.html'
                }
              }
          })
          .state('userSkillEdit/:skillId', {
              url: '/userSkillEdit/:skillId',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/userSkillEdit.html'
                }
              }
          })
          .state('userSkill', {
              url: '/userSkill',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/userSkill.html'
                }
              }
          })
          .state('userSkillEmpty', {
              url: '/userSkillEmpty',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/userSkillEmpty.html'
                }
              }
          })
          .state('feed', {
              url: '/feed',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/feed.html'
                }
              }
          })
          .state('contact', {
              url: '/contact',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/contact.html'
                }
              }
          })
          .state('adviser', {
              url: '/adviser',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/adviser.html'
                }
              }
          })
          .state('database', {
              url: '/database/:tagId',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/database.html'
                },
                "viewLeftBar": {
                    templateUrl: '/assets/partials/userTags.html'
                }
              }
          })
          .state('database.userTagCreate', {
              templateUrl: '/assets/partials/userTagCreate.html'
          })
          .state('database/tag', {
              url: '/database/tag/:userTagId',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/database.html'
                },
                "viewLeftBar": {
                    templateUrl: '/assets/partials/userTags.html'
                }
              }
          })
          .state('database/upload', {
              url: '/database/upload',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/databaseUpload.html'
                }
              }
          })
          .state('database/search', {
              url: '/database/search',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/databaseSearch.html'
                }
              }
          })
          .state('database/documentTag/', {
              url: '/database/documentTag/:documentId',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/databaseTag.html'
                }
              }
          })

###
angular.module('myApp.routeConfig', ['ngRoute'])
    .config ($routeProvider) ->
        $routeProvider
            .when('/', {
                templateUrl: '/assets/partials/industry.html'
            })
            .when('/industry', {
                templateUrl: '/assets/partials/industry.html'
            })
            .when('/skill', {
                templateUrl: '/assets/partials/skill.html'
            })
            .when('/domain', {
                templateUrl: '/assets/partials/domain.html'
            })
            .when('/userSkillCreate', {
                templateUrl: '/assets/partials/userSkillCreate.html'
            })
            .when('/userSkillEdit/:skillId', {
                templateUrl: '/assets/partials/userSkillEdit.html'
            })
            .when('/userSkill', {
                templateUrl: '/assets/partials/userSkill.html'
            })
            .when('/userSkillEmpty', {
                templateUrl: '/assets/partials/userSkillEmpty.html'
            })
            .when('/feed', {
                templateUrl: '/assets/partials/feed.html'
            })
            .when('/contact', {
                templateUrl: '/assets/partials/contact.html'
            })
            .when('/adviser', {
                templateUrl: '/assets/partials/adviser.html'
            })
            .when('/database', {
                templateUrl: '/assets/partials/database.html'
            })
            .when('/database/upload', {
                templateUrl: '/assets/partials/databaseUpload.html'
            })
            .when('/database/search', {
                templateUrl: '/assets/partials/databaseSearch.html'
            })
            .otherwise({redirectTo: '/'})
###
 
@commonModule = angular.module('myApp.common', [])
@controllersModule = angular.module('myApp.controllers', [])
@servicesModule = angular.module('myApp.services', [])
@modelsModule = angular.module('myApp.models', [])
@directivesModule = angular.module('myApp.directives', [])
@filtersModule = angular.module('myApp.filters', [])
