
dependencies = [
#    'ngRoute',
    'ngResource',
    'ngSanitize',
    'ui.bootstrap',
    'ui.bootstrap.datepicker',
    'ui.router',
    'ui.select2',
    'ct.ui.router.extras',
    'angularFileUpload',
    'smart-table',
    'ngDraggable',
    'ncy-angular-breadcrumb',
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
       $urlRouterProvider
          .when("", "/")
          .otherwise( ($injector, $location) ->
                                          $state = $injector.get('$state')
                                          $log = $injector.get('$log')
                                          $log.debug "in otherwise..."
                                          $state.go('folder.documents')
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
              },
              ncyBreadcrumb: {
                    label: 'Industries'
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
              },
              ncyBreadcrumb: {
                    parent: 'industries',
                    label: 'Create Industry'
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
              },
              ncyBreadcrumb: {
                    parent: 'industries',
                    label: 'Edit Industry'
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
              },
              ncyBreadcrumb: {
                    label: 'Skills'
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
              },
              ncyBreadcrumb: {
                    label: 'Domains'
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
              },
              ncyBreadcrumb: {
                    parent: 'domains',
                    label: 'Create Domain'
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
              },
              ncyBreadcrumb: {
                    parent: 'domains',
                    label: 'Edit Domain'
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
              },
              ncyBreadcrumb: {
                    label: 'Company'
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
              },
              ncyBreadcrumb: {
                    parent: 'companyView',
                    label: 'Edit Settings'
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
              },
              ncyBreadcrumb: {
                    parent: 'companyView',
                    label: 'Manage Users'
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
              },
              ncyBreadcrumb: {
                    parent: 'companyUsers',
                    label: 'Add User'
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
              },
              ncyBreadcrumb: {
                    parent: 'companyUsers',
                    label: 'View User'
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
              },
              ncyBreadcrumb: {
                    parent: 'companyUsers',
                    label: 'Edit User'
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
              },
              ncyBreadcrumb: {
                    label: 'Job Titles'
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
              },
              ncyBreadcrumb: {
                    parent: 'jobTitles',
                    label: 'Create Job Title'
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
              },
              ncyBreadcrumb: {
                    parent: 'jobTitles',
                    label: 'Edit Job Title'
              }
          })
          .state('jobRequirements', {
              url: '/jobs/list'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/jobRequirements.html'
                },
                'viewContextMenu@jobRequirements': {
                    templateUrl: '/assets/partials/contextMenuAdmin.html'
                }
              },
              ncyBreadcrumb: {
                    label: 'Jobs'
              }
          })
          .state('jobRequirementCreate', {
              url: '/jobs/job/create'
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
              },
              ncyBreadcrumb: {
                    parent: 'jobRequirements',
                    label: 'Create Job'
              }
          })
          .state('jobRequirementEdit', {
              url: '/jobs/job/:jobRequirementId/edit'
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
              },
              ncyBreadcrumb: {
                    parent: 'jobRequirements',
                    label: 'Edit Job'
              }
          })
          .state('jobRequirementPreview', {
              url: '/jobs/job/:jobRequirementId/preview'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/jobRequirementPreview.html'
                }
              },
              ncyBreadcrumb: {
                    parent: 'jobRequirements',
                    label: 'Preview Job'
              }
          })
          .state('jobRequirementView', {
              url: '/job/search/:jobRequirementId/view'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/jobRequirementView.html'
                }
              },
              ncyBreadcrumb: {
                    parent: 'jobSearch',
                    label: 'View Job'
              }
          })
          .state('jobApplication', {
              url: '/job/search/:jobRequirementId/apply'
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/jobApplication.html'
                }
              },
              ncyBreadcrumb: {
                    parent: 'jobRequirementView',
                    label: 'Apply Job'
              }
          })
          .state('jobSearch', {
              url: '/job/search',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    controller: 'JobSearchCtrl as ctrl',
                    templateUrl: '/assets/partials/jobSearch.html'
                }
              },
              ncyBreadcrumb: {
                    label: 'Job Search'
              }
          })
          .state('jobAdvanceSearch', {
              url: ''
              views: {
                'viewSearchResults': {
                    templateUrl: '/assets/partials/jobAdvanceSearch.html'
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
               'viewMain': {
                    templateUrl: '/assets/partials/contact.html'
                }
              },
              ncyBreadcrumb: {
                    label: 'Contacts'
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
                    controller: 'DatabaseCtrl as ctrl',
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
                    controller: 'DatabaseCtrl as ctrl',
                    templateUrl: '/assets/partials/databaseDocuments.html'
                }
              }
          })
          .state('folder', {
              url: '/document/folder/:documentFolderId',
              'abstract': true,
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    templateUrl: '/assets/partials/folder.html'
                },
                'viewContextMenu@folder': {
                    templateUrl: '/assets/partials/contextMenuFolderView.html'
                }
              }
          })
          .state('folder.documents', {
             # child of 'folder' state
              url: '',
              views: {
                'viewDocument': {
                    controller: 'FolderDocumentCtrl as ctrl',
                    templateUrl: '/assets/partials/databaseDocuments.html'
                }
              },
              ncyBreadcrumb: {
                    label: 'Folders'
              }
          })
          .state('folder.folderCreate', {
              views: {
                'viewTag': {
                    templateUrl: '/assets/partials/folderCreate.html'
                },
                'viewDocument': {
                    controller: 'FolderDocumentCtrl as ctrl',
                    templateUrl: '/assets/partials/databaseDocuments.html'
                }
              },
              ncyBreadcrumb: {
                    label: 'Folders'
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
              },
              ncyBreadcrumb: {
                    parent: 'folder.documents',
                    label: 'Upload'
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
              },
              ncyBreadcrumb: {
                    parent: 'folder.documents',
                    label: 'Search'
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
              },
              ncyBreadcrumb: {
                    parent: 'folder.documents',
                    label: 'XRay Document'
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
              },
              ncyBreadcrumb: {
                    parent: 'sharedRepositories',
                    label: 'XRay Document'
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
              },
              ncyBreadcrumb: {
                    parent: 'folder.documents',
                    label: 'View Document'
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
              },
              ncyBreadcrumb: {
                    parent: 'sharedRepositories',
                    label: 'View Document'
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
                    controller: 'DatabaseShareCtrl as ctrl',
                    templateUrl: '/assets/partials/databaseShare.html'
                }
              },
              ncyBreadcrumb: {
                    parent: 'databaseManageShares',
                    label: 'Share Document'
              }
          })
          .state('sharedDocumentShare', {
              url: '/shared/document/:documentId/share',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                  templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    controller: 'SharedDocumentShareCtrl as ctrl',
                    templateUrl: '/assets/partials/databaseShare.html'
                }
              },
              ncyBreadcrumb: {
                    parent: 'sharedRepositories',
                    label: 'Share Document'
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
                    controller: 'DatabaseManageShareCtrl as ctrl',
                    templateUrl: '/assets/partials/databaseManageShares.html'
                }
              },
              ncyBreadcrumb: {
                    parent: 'folder.documents',
                    label: 'Manage Shares'
              }
          })
           .state('sharedDocumentManageShares', {
              url: '/shared/document/:documentId/share/manage',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                  templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    controller: 'SharedDocumentManageShareCtrl as ctrl',
                    templateUrl: '/assets/partials/databaseManageShares.html'
                }
              }
          })
          .state('folder.shareFolder', {
              url: '/share',
              views: {
                'viewGlobalSearch': {
                },
                'viewContextMenu@folder': {
                    templateUrl: '/assets/partials/contextMenuGeneric.html'
                },
                'viewDocument': {
                    templateUrl: '/assets/partials/shareFolder.html'
                }
              },
              ncyBreadcrumb: {
                    parent: 'folder.documents',
                    label: 'Share Folder'
              }
          })
          .state('database.userTagManagement', {
              url: '/manage',
              views: {
                'viewDocument': {
                    templateUrl: '/assets/partials/userTagManagement.html'
                }
              }
          })
          .state('documentFolderManagement', {
              url: '/folders/manage',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                  templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                    controller: 'ManageFolderCtrl as ctrl',
                    templateUrl: '/assets/partials/folderManagement.html'
                }
              },
              ncyBreadcrumb: {
                    parent: 'folder.documents',
                    label: 'Manage Folders'
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
                'viewMain': {
                    templateUrl: '/assets/partials/sharedRepositories.html'
                }
              },
              ncyBreadcrumb: {
                    label: 'Shared Documents'
              }
          })
          .state('messages', {
              url: '/messages',
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
              url: '/list',
              views: {
                'viewMessageList': {
                  templateUrl: '/assets/partials/messageList.html'
                }
              },
              ncyBreadcrumb: {
                    label: 'Messages'
              }
          })
          .state('messages.createMessageBox', {
              url: '/message-box/create',
              views: {
                'viewLabel': {
                    templateUrl: '/assets/partials/messageBoxCreate.html'
                },
                'viewMessageList': {
                    templateUrl: '/assets/partials/messageList.html'
                }
              },
              ncyBreadcrumb: {
                    label: 'Messages'
              }
          })
          .state('manageMessageBox', {
              url: '/message-box/manage',
              views: {
                'viewHeaderBar': {
                  templateUrl: '/assets/partials/headerBarHome.html'
                },
                'viewMenuBar': {
                   templateUrl: '/assets/partials/menuBar.html'
                },
                'viewMain': {
                  templateUrl: '/assets/partials/messageBoxManagement.html'
                }
              },
              ncyBreadcrumb: {
                    parent: 'messages.messageList',
                    label: 'Manage Mail Boxes'
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
