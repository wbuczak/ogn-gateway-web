#!/bin/sh

HOME_DIR=`pwd`

mvn tomcat7:run 2>&1 > ${HOME_DIR}/out.log &
