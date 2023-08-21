# Knooppuntnet background tile generation and update

## Install docker and tools

Install docker: (instructions from https://docs.docker.com/engine/install/ubuntu/)

    sudo apt-get update
    sudo apt-get install \
      apt-transport-https \
      ca-certificates \
      curl \
      gnupg-agent \
      software-properties-common
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
    sudo apt-key fingerprint 0EBFCD88
    sudo add-apt-repository \
      "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
      $(lsb_release -cs) \
      stable"
    sudo apt-get update
    sudo apt-get install docker-ce docker-ce-cli containerd.io
    sudo apt install docker-compose


  view status:
    sudo systemctl status docker
    docker -v

  sudo usermod -aG docker marcv
  docker container run hello-world

Install bc (arbitrary precision calculator language - called from quickstart.sh):

    sudo apt-get install bc

Install osmium (used to merge pbf files and to calculate bounds):

    sudo apt install osmium-tool



## openmaptiles

Documented on https://openmaptiles.org/docs/generate/generate-openmaptiles/

Reset previous installation:

    cd /kpn/openmaptiles
    make destroy-db
    cd ..
    rm -rf /kpn/openmaptiles

Installation:

      cd /kpn
      git clone https://github.com/vmarc/openmaptiles.git
      cd openmaptiles
      mkdir data
    
      docker-compose pull
      ./quickstart.sh ==> to test

## Update procedure

Reset previous database contents:

    cd /kpn/openmaptiles
    make destroy-db
    cd /kpn/tile-data
    rm *.pbf


Download and merge OpenStreetMap data from geofabrik:

    /kpn/scripts/01-download.sh

ll -h *.pbf

    -rw-rw-r-- 1 marcv marcv  11G Jan 16 09:27 all.osm.pbf
    -rw-rw-r-- 1 marcv marcv 606M Jan 16 02:19 austria-latest.osm.pbf
    -rw-rw-r-- 1 marcv marcv 404M Jan 16 02:13 belgium-latest.osm.pbf
    -rw-rw-r-- 1 marcv marcv 3.7G Jan 16 02:13 france-latest.osm.pbf
    -rw-rw-r-- 1 marcv marcv 3.3G Jan 16 02:19 germany-latest.osm.pbf
    -rw-rw-r-- 1 marcv marcv  30M Jan 16 02:19 luxembourg-latest.osm.pbf
    -rw-rw-r-- 1 marcv marcv 1.1G Jan 16 02:13 netherlands-latest.osm.pbf
    -rw-rw-r-- 1 marcv marcv 890M Jan 16 02:13 spain-latest.osm.pbf



Only first time:

    Get bounding box:
    
        osmium fileinfo -eg data.bbox all.osm.pbf

        (-20.0712,27.6272,29.9753,62.5147)
        LONG1,LAT1,LONG2,LAT2

        https://www.latlong.net/c/?lat=27.6&long=-20
        https://www.latlong.net/c/?lat=62.5&long=30

        cannot use this bounding box: too wide (caused by overseas parts of France?)
    
        use this manually determined BBOX instead:

        left -9.3 bottom 36.0 right 24.5 top 55.15
        BBOX=-9.3,36.0,24.5,55.15 // includes Poland
        BBOX=-9.3,36.0,17.2,55.15
        https://www.latlong.net/c/?lat=36.0&long=-9.3
        https://www.latlong.net/c/?lat=55.15&long=24.5


    Update .env file with bounding box.
    
    Update .env file with max zoom level 14.

Currently used bounding box:


    http://bboxfinder.com/#36,-9.3,55.15,17.2 // original
    http://bboxfinder.com/#36,-9.3,55.15,24.5 // includes Poland
    http://bboxfinder.com/#36,-9.3,57.76,17.2 // includes Denmark


Prepare:

  cd /kpn/openmaptiles/data
	rm -rf *

	mv /kpn/tile-data/all.osm.pbf .

Import data:

	/kpn/scripts/02-import.sh 2 hours, 16 minutes

(Re-)Execute sql scripts:

	/kpn/scripts/03-sql.sh  # 4.5 hours


Change MIN_ZOOM to 4 and MAX_ZOOM to 12 in

	.env

Adapt data/all.dc-config.yml, default zoom levels:

	cp all.dc-config.yml /kpn/conf/tiles-config-12.yml
	cp all.dc-config.yml /kpn/conf/tiles-config-13.yml
	cp all.dc-config.yml /kpn/conf/tiles-config-14.yml

Generate tiles:

	/kpn/scripts/04-generate-tiles.sh     # 5 hours

Remove the old tiles:

	cd /kpn/tiles-install
  rm -rf *

Unpack the tiles:

	/kpn/soft/mbutil/mb-util /kpn/openmaptiles/data/tiles.mbtiles osm --image_format=pbf >> /kpn/logs/mbutil.log 2>&1 

Make productive:

    find /kpn/tiles-install/osm -type f | wc -l
    find /kpn/tiles/osm -type f | wc -l
    nohup rsync -av  --human-readable --progress --delete /kpn/tiles-install/osm/ /kpn/tiles/osm >> /kpn/logs/tiles-rsync.log 2>&1 &
    find /kpn/tiles/osm -type f | wc -l

Opendata sync:

    rsync -av  --human-readable --progress --delete /kpn/tiles/opendata marcv@kpn-analysis:/kpn/tiles
