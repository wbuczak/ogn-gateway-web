#!/usr/bin/python

import cgi
from glob import glob

params = cgi.FieldStorage();
date = params["date"].value 

files = glob("/igc/"+date+"/*.IGC")

table_data = []
 
for f in files:
   fname = f.split("/")[-1]
   table_data.append(fname)

print "Content-Type: text/html"
print

print """

<!doctype html>
<html lang="en" ng-app="myApp">
<head>
  <meta charset="utf-8">
  <title>OGN IGC log files</title>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular.js/1.2.1/angular.min.js"></script>
  <script src="/dirPagination.js"></script>
  <script type="text/javascript" src="/func.js"></script>
  
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
for f in table_data[:-1]:
   print "{ file:'"+f+"'},"
print "{ file:'"+table_data[-1]+"'}"

print """
           ];

           $scope.sortField = 'file';
           $scope.reverse = true;
           
           $scope.greaterThan = function(prop, val){
             return function(item){
               return item[prop] > val;
             }
           }
"""
print "$scope.ldate="+"'"+date+"'"

print """

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
        <th><a href="" ng-click="sortField = 'file'; reverse = !reverse">file</a></th> <th></th>
      </tr>
      <tr dir-paginate="d in data | filter:query | orderBy:sortField:reverse | itemsPerPage:100" ng-class-odd="'odd'" ng-class-even="'even'">
        <td> <a href="../igc/{{ldate}}/{{d.file}}" download> {{d.file}} </a> </td>
        <td> <a href="http://cunimb.net/igc2map.php?lien=http://ognstats.ddns.net/igc/{{ldate}}/{{d.file}}"> &nbsp;&nbsp;[ M1 ]</a> </td>
        <td> <a href="http://www.victorb.fr/visugps/visugps.html?track=http://ognstats.ddns.net/igc/{{ldate}}/{{d.file}}"> &nbsp;&nbsp;[ M2 ]</a> </td>
      </tr>
  </table>
 </div>

</body>
</html>
"""
