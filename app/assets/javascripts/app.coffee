
dependencies = [
#    'ngRoute',
    'ngResource',
    'ngSanitize',
    'ui.bootstrap',
    'ui.bootstrap.datepicker',
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
    .config ($stateProvider, $urlRouterProvider, $httpProvider) ->
       $httpProvider.interceptors.push(['$log', '$rootScope', '$q', 'ErrorService', ($log, $rootScope, $q, ErrorService) ->
              {
                responseError: (rejection) =>
                    $log.error("Intercepted response error #{angular.toJson(rejection)}")
                    if (rejection.status is 401)
                        $q.reject(rejection)
                        window.location.href = '/logout'
                        #$rootScope.$broadcast('event:loginRequired');
                    else if (rejection.data)
                        ErrorService.error(rejection.data.message) if rejection.data.message 
                    # otherwise, default behavior
                    $q.reject(rejection)
              }
       ])
       $urlRouterProvider.otherwise( ($injector, $location) ->
                                          $state = $injector.get('$state')
                                          $log = $injector.get('$log')
                                          #$log.debug "in otherwise..."
                                          $state.go('index')
                                    )
       $stateProvider
          .state('settings', {
              url: '/settings',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarSettings.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/userProfileSettings.html'
                }
              }
          })
          .state('index', {
              url: '/index',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                }
              }
          })
          .state('industries', {
              url: '/industry',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewContextMenu@industries': {
                    templateUrl: '/assets/partials/contextMenuAdmin.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/industry.html'
                }
              }
          })
          .state('industryCreate', {
              url: '/industry/create'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/industryCreate.html'
                }
              }
          })
          .state('industryEdit', {
              url: '/industry/edit/:industryId'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/industryEdit.html'
                }
              }
          })
          .state('skill', {
              url: '/skill',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewContextMenu@skill': {
                    templateUrl: '/assets/partials/contextMenuAdmin.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/skill.html'
                }
              }
          })
          .state('domains', {
              url: '/domain',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewContextMenu@domains': {
                    templateUrl: '/assets/partials/contextMenuAdmin.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/domain.html'
                }
              }
          })
          .state('domainCreate', {
              url: '/domain/create'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/domainCreate.html'
                }
              }
          })
          .state('domainEdit', {
              url: '/domain/edit/:domainId'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/domainEdit.html'
                }
              }
          })
          .state('companyCreate', {
              url: '/company/create'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/companyCreate.html'
                }
              }
          })
          .state('companyView', {
              url: '/company/view'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/companyView.html'
                }
              }
          })
          .state('companyEdit', {
              url: '/company/edit'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/companyEdit.html'
                }
              }
          })
          .state('companyUsers', {
              url: '/companyUser'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewContextMenu@companyUsers': {
                    templateUrl: '/assets/partials/contextMenuCompany.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/companyUsers.html'
                }
              }
          })
          .state('companyUserCreate', {
              url: '/companyUser/create'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewContextMenu@companyUsers': {
                    templateUrl: '/assets/partials/contextMenuCompany.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/companyUserCreate.html'
                }
              }
          })
          .state('companyUserView', {
              url: '/companyUser/:companyUserId/view'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewContextMenu@companyUsers': {
                    templateUrl: '/assets/partials/contextMenuCompany.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/companyUserView.html'
                }
              }
          })
          .state('companyUserEdit', {
              url: '/companyUser/:companyUserId/edit'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewContextMenu@companyUsers': {
                    templateUrl: '/assets/partials/contextMenuCompany.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/companyUserEdit.html'
                }
              }
          })
          .state('jobTitles', {
              url: '/jobTitle',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/jobTitle.html'
                },
                'viewContextMenu@jobTitles': {
                    templateUrl: '/assets/partials/contextMenuAdmin.html'
                }
              }
          })
          .state('jobTitleCreate', {
              url: '/jobTitle/create'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/jobTitleCreate.html'
                }
              }
          })
          .state('jobTitleEdit', {
              url: '/jobTitle/edit/:jobTitleId'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/jobTitleEdit.html'
                }
              }
          })
          .state('jobRequirements', {
              url: '/jobs'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewGlobalSearch@jobRequirements': {
                    templateUrl: '/assets/partials/globalSearch.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/jobRequirements.html'
                },
                'viewContextMenu@jobRequirements': {
                    templateUrl: '/assets/partials/contextMenuAdmin.html'
                }
              }
          })
          .state('jobRequirementCreate', {
              url: '/jobs/create'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/jobRequirementCreate.html'
                }
              }
          })
          .state('jobRequirementEdit', {
              url: '/jobs/edit/:jobRequirementId'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/jobRequirementEdit.html'
                }
              }
          })
          .state('userSkillCreate', {
              url: '/userSkillCreate',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/userSkillCreate.html'
                }
              }
          })
          .state('userSkillEdit/:skillId', {
              url: '/userSkillEdit/:skillId',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/userSkillEdit.html'
                }
              }
          })
          .state('userSkill', {
              url: '/userSkill',
              views: {
                'viewMain': {
                    templateUrl: '/assets/partials/userSkill.html'
                }
              }
          })
          .state('userSkillEmpty', {
              url: '/userSkillEmpty',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/userSkillEmpty.html'
                }
              }
          })
          .state('feed', {
              url: '/feed',
              views: {
                'viewMain': {
                    templateUrl: '/assets/partials/feed.html'
                }
              }
          })
          .state('contact', {
              url: '/contact',
              views: {
               'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
               'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
               'viewContextMenu@contact': {
                    templateUrl: '/assets/partials/contextMenuGeneric.html'
                },
               'viewGlobalSearch@contact': {
                    templateUrl: '/assets/partials/globalSearch.html'
                },
               'viewMain': {
                    templateUrl: '/assets/partials/contact.html'
                }
              }
          })
          .state('contactInvite', {
              url: '/contact/invite/:contactId',
              views: {
               'viewMain': {
                    templateUrl: '/assets/partials/contactInvite.html'
                }
              }
          })
          .state('adviser', {
              url: '/adviser',
              views: {
                'viewMain': {
                    templateUrl: '/assets/partials/adviser.html'
                }
              }
          })
          .state('database', {
              url: '/database/tag/:userTagId',
              'abstract': true,
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewGlobalSearch@database': {
                    templateUrl: '/assets/partials/globalSearch.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/database.html'
                },
                'viewContextMenu@database': {
                    templateUrl: '/assets/partials/contextMenuDatabase.html'
                }
              }
          })
          .state('database.documents', {
             # child of 'database' state
              url: '',
              views: {
                'viewDocument': {
                    templateUrl: '/assets/partials/databaseDocuments.html'
                }
              }
          })
          .state('database.userTagCreate', {
              views: {
                'viewTag': {
                    templateUrl: '/assets/partials/userTagCreate.html'
                },
                'viewDocument': {
                    templateUrl: '/assets/partials/databaseDocuments.html'
                }
              }
          })
          .state('databaseUpload', {
              url: '/database/document/upload',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/databaseUpload.html'
                }
              }
          })
          .state('databaseSearch', {
              url: '/database/document/search',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewGlobalSearch': {
                    templateUrl: '/assets/partials/globalSearch.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/databaseSearch.html'
                }
              }
          })
          .state('databaseDocumentTag', {
              url: '/document/tag/:documentId',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/databaseTag.html'
                }
              }
          })
          .state('databaseDocumentEdit', {
              url: '/document/edit/:documentId',
              views: {
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/databaseEdit.html'
                }
              }
          })
          .state('databaseDocumentXRay', {
              url: '/document/xray/:documentId',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/databaseXRay.html'
                }
              }
          })
          .state('sharedDocumentXRay', {
              url: '/shared/document/xray/:documentId',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/sharedDocumentXRay.html'
                }
              }
          })
          .state('databaseDocumentView', {
              url: '/document/view/:documentId',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/databaseView.html'
                }
              }
          })
          .state('sharedDocumentView', {
              url: '/shared/document/view/:documentId',
              views: {
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/sharedDocumentView.html'
                }
              }
          })
          .state('databaseDocumentShare', {
              url: '/document/share/:documentId',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                  templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/databaseShare.html'
                }
              }
          })
          .state('databaseManageShares', {
              url: '/document/share/:documentId/manage',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                  templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/databaseManageShares.html'
                }
              }
          })
          .state('shareRepository', {
              url: '/document/repository/share',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/shareRepository.html'
                }
              }
          })
          .state('database.userTagManagement', {
              url: '/database/tags',
              views: {
                'viewDocument': {
                    templateUrl: '/assets/partials/userTagManagement.html'
                }
              }
          })
          .state('sharedRepositories', {
              url: '/repository/shared/document',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewContextMenu@sharedRepositories': {
                    templateUrl: '/assets/partials/contextMenuGeneric.html'
                },
                'viewGlobalSearch@sharedRepositories': {
                    templateUrl: '/assets/partials/globalSearch.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/sharedRepositories.html'
                }
              }
          })
          .state('messages', {
              url: '/message',
              'abstract': true,
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewContextMenu@messages': {
                    templateUrl: '/assets/partials/contextMenuGeneric.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/message.html'
                }
              }
          })
          .state('messages.messageList', {
              # child of 'message' state
              url: '',
              views: {
                'viewMessageList': {
                  templateUrl: '/assets/partials/messageList.html'
                }
              }
          })
          .state('messages.createMessageBox', {
              url: '/create',
              views: {
                'viewLabel': {
                    templateUrl: '/assets/partials/messageBoxCreate.html'
                },
                'viewMessageList': {
                    templateUrl: '/assets/partials/messageList.html'
                }
              }
          })
          .state('messages.manageMessageBox', {
              url: '/manage',
              views: {
                'viewMessageList': {
                  templateUrl: '/assets/partials/messageBoxManagement.html'
                }
              }
          })
          .state('groups', {
            url: '/login'
          })
 
@commonModule = angular.module('myApp.common', [])
@controllersModule = angular.module('myApp.controllers', [])
@servicesModule = angular.module('myApp.services', [])
@modelsModule = angular.module('myApp.models', [])
@directivesModule = angular.module('myApp.directives', [])
@filtersModule = angular.module('myApp.filters', [])
