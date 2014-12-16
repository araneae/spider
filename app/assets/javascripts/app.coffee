
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
                'viewRightBar': {
                  templateUrl: '/assets/partials/rightBar.html'
                }
              }
          })
          .state('industries', {
              url: '/admin/industry',
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
              url: '/admin/industry/create'
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
              url: '/admin/industry/:industryId/edit'
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
              url: '/admin/domain',
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
              url: '/admin/domain/create'
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
              url: '/admin/domain/:domainId/edit'
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
              url: '/admin/company/view'
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
              url: '/admin/company/edit'
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
              url: '/admin/company/user'
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
              url: '/admin/company/user/create'
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
              url: '/admin/company/user/:companyUserId/view'
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
              url: '/company/user/:companyUserId/edit'
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
              url: '/admin/jobTitle',
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
              url: '/admin/jobTitle/create'
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
              url: '/admin/jobTitle/:jobTitleId/edit'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
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
              url: '/jobs/:jobRequirementId/edit'
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
              url: '/user/skill/create',
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
              url: '/user/skill/:skillId/edit',
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
              url: '/user/skill',
              views: {
                'viewMain': {
                    templateUrl: '/assets/partials/userSkill.html'
                }
              }
          })
          .state('userSkillEmpty', {
              url: '/user/skill/empty',
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
              url: '/contact/:contactId/invite',
              views: {
               'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
               'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
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
              url: '/document/tag/:userTagId',
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
              url: '/document/upload',
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
              url: '/document/search',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewGlobalSearch@databaseSearch': {
                    templateUrl: '/assets/partials/globalSearch.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/databaseSearch.html'
                }
              }
          })
          .state('databaseDocumentTag', {
              url: '/document/:documentId/tag',
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
              url: '/document/:documentId/edit',
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
              url: '/document/:documentId/xray',
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
              url: '/shared/document/:documentId/xray',
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
              url: '/document/:documentId/view',
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
              url: '/shared/document/:documentId/view',
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
              url: '/document/:documentId/share',
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
              url: '/document/:documentId/share/manage',
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
              url: '/document/tags/manage',
              views: {
                'viewDocument': {
                    templateUrl: '/assets/partials/userTagManagement.html'
                }
              }
          })
          .state('sharedRepositories', {
              url: '/shared/document',
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
