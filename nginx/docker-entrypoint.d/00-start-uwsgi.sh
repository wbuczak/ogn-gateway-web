#!/bin/bash
set -e

echo "Starting uwsqi.."
sudo -u www-data nohup /usr/local/bin/uwsgi /etc/nginx/uwsgi_config.ini &