# Knooppuntnet background tile generation and update

## docker

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


Download OpenStreetMap data from geofabrik:

    mkdir /kpn/tile-data // if not exists
    cd /kpn/tile-data
    rm *.pbf
    ./download.sh    # 3 minutes
    ./merge.sh       # 10 minutes

Contents of download.sh:

	wget http://download.geofabrik.de/europe/belgium-latest.osm.pbf
	wget http://download.geofabrik.de/europe/netherlands-latest.osm.pbf
	wget http://download.geofabrik.de/europe/germany-latest.osm.pbf
	wget http://download.geofabrik.de/europe/france-latest.osm.pbf
	wget http://download.geofabrik.de/europe/austria-latest.osm.pbf
	wget http://download.geofabrik.de/europe/spain-latest.osm.pbf
	wget http://download.geofabrik.de/europe/luxembourg-latest.osm.pbf

Contents of merge.sh:

	osmium merge \
	  belgium-latest.osm.pbf \
	  netherlands-latest.osm.pbf \
	  germany-latest.osm.pbf \
	  france-latest.osm.pbf \
	  austria-latest.osm.pbf \
	  spain-latest.osm.pbf \
      luxembourg-latest.osm.pbf \
	  -o all.osm.pbf


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

        left -9.3 bottom 36.0 right 17.3 top 55.15
        BBOX=-9.3,36.0,17.3,55.15
        https://www.latlong.net/c/?lat=36.0&long=-9.3
        https://www.latlong.net/c/?lat=55.15&long=17.3


    Update .env file with bounding box.
    
    Update .env file with max zoom level 14.

Prepare:

    cd /kpn/openmaptiles
	rm data/all.osm.pbf
	rm data/tiles.mbtiles*

	mv /kpn/tile-data/all.osm.pbf data/

Start up the database container:

	/kpn/scripts/01-start-db.sh  # interactive, very fast

Import external data:

	/kpn/scripts/02-import-data.sh  # interactive, some minutes

Import OSM data:

	/kpn/scripts/03-import-osm.sh       # 18 hours
	/kpn/scripts/04-import-borders.sh   # 6 minutes
	/kpn/scripts/05-import-wikidata.sh  # 4 minutes

(Re-)Execute sql scripts:

	/kpn/scripts/06-import-sql.sh  # 3 hours   ---> last time 14 hours (TODO increase swap?)


Change MIN_ZOOM to 4 and MAX_ZOOM to 12 in

	.env

Determine bounding box (data/all.dc-config.yml) and prepare for generating tiles:

	# not needed trueanymore?, using configuration files on /kpn/conf
	/kpn/scripts/07-generate-dc-config.sh  # 5 minutes

Adapt data/all.dc-config.yml, default zoom levels:

	cp all.dc-config.yml /kpn/conf/tiles-config-12.yml
	cp all.dc-config.yml /kpn/conf/tiles-config-13.yml
	cp all.dc-config.yml /kpn/conf/tiles-config-14.yml

Generate tiles:

	/kpn/scripts/08-generate-tiles-12.sh     # 6 hours / last time: 19 hours

Generate zoom level 13:

	/kpn/scripts/09-generate-tiles-13.sh     # 22 hours / last time: 20 hours (swapfile 10G)

Generate zoom level 14:

	/kpn/scripts/10-generate-tiles-14.sh    # almost 3.5 days

Copy tiles to kpn server, on kpn server:

	cd /kpn/tiles-install
	scp kpn-tiles:/kpn/openmaptiles/data/tiles.mbtiles .

On kpn server:

	cd /kpn/tiles-install
	/kpn/soft/mbutil/mb-util tiles.mbtiles osm --image_format=pbf >> /kpn/logs/mbutil.log 2>&1 

	mkdir osm
	mv t12/? osm

Make productive:

    find /kpn/tiles-install/osm -type f | wc -l
    find /kpn/tiles/osm -type f | wc -l
    nohup rsync -av  --human-readable --progress --delete  \
      /kpn/tiles-install/osm/ \ # note: the slash at the end is important
      /kpn/tiles/osm >> /kpn/logs/tiles-rsync.log 2>&1 &
    find /kpn/tiles/osm -type f | wc -l
