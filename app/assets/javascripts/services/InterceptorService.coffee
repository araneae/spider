

servicesModule.factory('InterceptorService', ['$q', '$log', ($q, $log) -> 
        {
          'responseError': (rejection) =>
            $q.reject(rejection)
        }
      ]
)
  
