#!/usr/bin/python

import cgi
import cgitb
#import HTML
from glob import glob

#cgitb.enable()

dirs = glob("/var/www/html/igc/*")

table_data = []
  
for d in dirs:
   dname = d.split("/")[-1]
   table_data.append(dname)

print "Content-Type: text/html"
print

print """

<!doctype html>
<html lang="en" ng-app="myApp">
<head>
  <meta charset="utf-8">
  <title>OGN IGC log folders</title>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.2.1/angular.min.js"></script>
  <script src="dirPagination.js"></script>
  <script type="text/javascript" src="func.js"></script>
  
  <!-- Latest compiled and minified CSS -->
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"
  integrity="sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7"
  crossorigin="anonymous"/>

  <script>
     var myApp = angular.module('myApp', ['angularUtils.directives.dirPagination']);
     
     myApp.config(function($httpProvider) {
          $httpProvider.defaults.useXDomain = true;
     });
     
     myApp.controller('Ctrl1', function($scope) {
           $scope.data = [ 
"""

for d in table_data[:-1]:
   print "{ date:'"+d+"'},"
print "{ date:'"+table_data[-1]+"'}"

print """
           ];
           
           $scope.sortField = 'date';
           $scope.reverse = true;
           
           $scope.greaterThan = function(prop, val){
             return function(item){
               return item[prop] > val;
             }
           }
     });

  </script>

 <style type="text/css">
    .odd {
       background-color:#eee;
    }
    .even {
       background-color:#fff;
    }

    th {
       cursor:pointer;
       padding: 2px;
    }
    td {
       padding: 2px;
    }
  </style>

</head>

 <body>

<div>
 <dir-pagination-controls
       max-size="5"
       direction-links="true"
       boundary-links="true" >
 </dir-pagination-controls>
</div>

<div ng-controller="Ctrl1">
  Search: <input ng-model="query" type="text" />
  <table>
      <tr>
        <th><a href="" ng-click="sortField = 'date'; reverse = !reverse">date</a></th>      
      </tr>
      <tr dir-paginate="d in data | filter:query | orderBy:sortField:reverse | itemsPerPage:20" ng-class-odd="'odd'" ng-class-even="'even'">
        <td> <a href="igc-list-files.py?date={{d.date}}">{{d.date}}</a> </td>
      </tr>
  </table>
</div>

</body>
</html>
"""
