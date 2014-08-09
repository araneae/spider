
dependencies = [
#    'ngRoute',
    'ngResource',
    'ngSanitize',
    'ui.bootstrap',
    'ui.router',
    'ui.select2',
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
             # child of 'database' state
              url: '',
              views: {
                "viewDocument": {
                    templateUrl: '/assets/partials/databaseDocuments.html'
                }
              }
          })
          .state('sharedDatabase', {
              url: '/database/shared',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/sharedDatabase.html'
                }
              }
          })
          .state('sharedDatabase.documentShare', {
              url: '/share/:documentId',
              views: {
                '': {
                    templateUrl: '/assets/partials/sharedDatabaseShare.html'
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
          .state('database.documents.documentTag', {
              url: '/document/tag/:documentId',
              views: {
                '': {
                    templateUrl: '/assets/partials/databaseTag.html'
                }
              }
          })
          .state('database.documents.documentEdit', {
              url: '/document/edit/:documentId',
              views: {
                '': {
                    templateUrl: '/assets/partials/databaseEdit.html'
                }
              }
          })
          .state('database.documents.documentXRay', {
              url: '/document/xray/:documentId',
              views: {
                '': {
                    templateUrl: '/assets/partials/databaseXRay.html'
                }
              }
          })
          .state('database.documents.documentShare', {
              url: '/document/share/:documentId',
              views: {
                '': {
                    templateUrl: '/assets/partials/databaseShare.html'
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
          .state('messages', {
              url: '/message',
              views: {
                "viewMain": {
                    templateUrl: '/assets/partials/message.html'
                }
              }
          })
 
@commonModule = angular.module('myApp.common', [])
@controllersModule = angular.module('myApp.controllers', [])
@servicesModule = angular.module('myApp.services', [])
@modelsModule = angular.module('myApp.models', [])
@directivesModule = angular.module('myApp.directives', [])
@filtersModule = angular.module('myApp.filters', [])
