# Set nginx base image
FROM nginx

# File Author / Maintainer
MAINTAINER Wojtek Buczak

ENV DEBIAN_FRONTEND noninteractive

RUN set -x \
	&& apt-get update \
	&& apt-get install -y procps \
	&& apt-get install -y --no-install-recommends apt-utils \
	&& apt-get install -y sudo \
	&& apt-get install -y build-essential \
	&& apt-get install -y make \
	&& apt-get install -y python \
	&& apt-get install --no-install-recommends --no-install-suggests -y curl \
	&& \
	curl http://uwsgi.it/install | bash -s cgi /usr/local/bin/uwsgi

# Uninstall unecessary stuff
RUN set -x \        
	&& apt-get remove -y build-essential \
	&& apt-get remove -y make \
	&& apt-get -y autoremove \
	&& apt-get remove -y python \
	&& apt-get remove -y curl
	

# Copy custom configuration file from the current directory
COPY nginx.conf uwsgi_config.ini /etc/nginx/

RUN mkdir /usr/lib/cgi-bin
COPY hello.py igc-list-dirs.py igc-list-files.py /usr/lib/cgi-bin/
RUN chmod +x /usr/lib/cgi-bin/*.py

RUN mkdir /igc

EXPOSE 80

COPY docker-entrypoint.sh /
COPY docker-entrypoint.d/* /docker-entrypoint.d/
ONBUILD COPY docker-entrypoint.d/* /docker-entrypoint.d/

ENTRYPOINT ["/docker-entrypoint.sh"]

CMD ["nginx", "-g", "daemon off;"]