
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
              url: '/database/tag/:userTagId',
              'abstract': true,
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/database.html'
                }
              }
          })
          .state('database.documents', {
              url: '',
              views: {
                "viewDocument": {
                    templateUrl: '/assets/partials/databaseDocuments.html'
                }
              }
          })
          .state('database.userTagCreate', {
              views: {
                "viewTag": {
                    templateUrl: '/assets/partials/userTagCreate.html'
                },
                "viewDocument": {
                    templateUrl: '/assets/partials/databaseDocuments.html'
                }
              }
          })
          .state('databaseUpload', {
              url: '/database/document/upload',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/databaseUpload.html'
                }
              }
          })
          .state('databaseSearch', {
              url: '/database/document/search',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/databaseSearch.html'
                }
              }
          })
          .state('databaseDocumentTag', {
              url: '/database/documentTag/:documentId',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/databaseTag.html'
                }
              }
          })
          .state('userTagManagement', {
              url: '/database/tags',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/userTagManagement.html'
                }
              }
          })
 
@commonModule = angular.module('myApp.common', [])
@controllersModule = angular.module('myApp.controllers', [])
@servicesModule = angular.module('myApp.services', [])
@modelsModule = angular.module('myApp.models', [])
@directivesModule = angular.module('myApp.directives', [])
@filtersModule = angular.module('myApp.filters', [])
