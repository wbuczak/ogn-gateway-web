#!/usr/bin/python

import cgi
import cgitb
import HTML
from glob import glob

cgitb.enable()

params = cgi.FieldStorage();
date = params["date"].value 

files = glob("/var/www/html/igc/"+date+"/*.IGC")

table_data = []


igc2mapUrlPrefix="http://cunimb.net/igc2map.php?lien=http://ognstats.ddns.net:8088/igc/"+date +"/"
 
for f in files:
   fname = f.split("/")[-1]
   table_data.append([ HTML.link( fname ,"../igc/"+date+"/"+fname), HTML.link( 'M', igc2mapUrlPrefix+fname) ] )

htmlcode = HTML.table(table_data)

print "Content-Type: text/html"
print

print """

<!doctype html>
<html>
 <head/>
 <body>
"""

print htmlcode

print """
 </body>
</html>
"""
