#!/bin/bash
set -e

# TODO: improve: https://www.camptocamp.com/en/actualite/flexible-docker-entrypoints-scripts/

#if [ "$1" = 'nginx' ]; then
echo "Starting uwsqi.."
sudo -u www-data /usr/local/bin/uwsgi /etc/nginx/uwsgi_config.ini &
#  /usr/sbin/nginx -g 'daemon off;'
#fi

echo "running $@"
exec "$@"