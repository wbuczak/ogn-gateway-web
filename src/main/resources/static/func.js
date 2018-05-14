var params = function() {
       function urldecode(str) {
           return decodeURIComponent((str+'').replace(/\+/g, '%20'));
       }

       function transformToAssocArray( prmstr ) {
           var params = {};
           var prmarr = prmstr.split("&");
           for ( var i = 0; i < prmarr.length; i++) {
               var tmparr = prmarr[i].split("=");
               params[tmparr[0]] = urldecode(tmparr[1]);
           }
           return params;
       }

       var prmstr = window.location.search.substr(1);
       return prmstr != null && prmstr != "" ? transformToAssocArray(prmstr) : {};
     }();
	 
function humanFileSize(size) {
    var i = Math.floor( Math.log(size) / Math.log(1024) );
    return ( size / Math.pow(1024, i) ).toFixed(2) * 1 + ' ' + ['B', 'kB', 'MB', 'GB', 'TB'][i];
};
