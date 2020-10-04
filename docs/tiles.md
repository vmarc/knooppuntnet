Install docker: (instructions from https://linuxize.com/post/how-to-install-and-use-docker-on-ubuntu-18-04/)

  sudo apt update
  sudo apt upgrade
  sudo apt install apt-transport-https ca-certificates curl software-properties-common
  curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
  sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
  sudo apt update
  sudo apt install docker-ce
  sudo apt install docker-compose

  view status:
    sudo systemctl status docker
    docker -v

  sudo usermod -aG docker marcv
  docker container run hello-world

Documented on https://openmaptiles.org/docs/generate/generate-openmaptiles/

  cd /kpn
  git clone https://github.com/openmaptiles/openmaptiles.git
  cd openmaptiles

  // Add tile-data to .gitignore

  git checkout  6ac544fc9610b5 // older version to avoid memory problem (https://github.com/openmaptiles/openmaptiles/issues/938)

  docker-compose pull
  ./quickstart.sh ==> to test

Update procedure:

  mkdir ~/tile-data // if not exists
  cd ~/tile-data
  mkdir old
  mv *.pbf old
  ./download.sh
  ./merge.sh

Contents of download.sh:

	wget http://download.geofabrik.de/europe/belgium-latest.osm.pbf
	wget http://download.geofabrik.de/europe/netherlands-latest.osm.pbf
	wget http://download.geofabrik.de/europe/germany-latest.osm.pbf
	wget http://download.geofabrik.de/europe/france-latest.osm.pbf
	wget http://download.geofabrik.de/europe/austria-latest.osm.pbf
	wget http://download.geofabrik.de/europe/spain-latest.osm.pbf

Contents of merge.sh:

	osmium merge \
	  belgium-latest.osm.pbf \
	  netherlands-latest.osm.pbf \
	  germany-latest.osm.pbf \
	  france-latest.osm.pbf \
	  austria-latest.osm.pbf \
	  spain-latest.osm.pbf \
	  -o all.osm.pbf


ll -h *.pbf

	-rw-rw-r-- 1 marcv marcv 9.8G Jul 15 17:19 all.osm.pbf
	-rw-rw-r-- 1 marcv marcv 587M Jul 15 02:41 austria-latest.osm.pbf
	-rw-rw-r-- 1 marcv marcv 371M Jul 15 02:17 belgium-latest.osm.pbf
	-rw-rw-r-- 1 marcv marcv 3.7G Jul 15 02:17 france-latest.osm.pbf
	-rw-rw-r-- 1 marcv marcv 3.2G Jul 15 02:41 germany-latest.osm.pbf
	-rw-rw-r-- 1 marcv marcv 1.1G Jul 15 02:17 netherlands-latest.osm.pbf
	-rw-rw-r-- 1 marcv marcv 843M Jul 15 02:17 spain-latest.osm.pbf


//Get bounding box:
//
//	osmium fileinfo -eg data.bbox all.osm.pbf
//
//Update .env file with bounding box.
//
//Update .env file with max zoom level 14.

Remove following line from openmaptiles.yaml:

	layers/poi/poi.yaml

Prepare:

	rm -rf data
	mkdir -p data

	mv ~/tile-data/all.osm.pbf data/

//Start:
//
//	nohup ./quickstart.sh all  &               // gestart op 2020-07-15 17:50
//	                                           // opnieuw gestart op 2020-07-16 08:30

Start up the database container:

	/kpn/scripts/01-start-db.sh  # interactive, very fast

Import external data:

	/kpn/scripts/02-import-data.sh  # interactive, some minutes

Import OSM data:

	/kpn/scripts/03-import-osm.sh       # 11 hours
	/kpn/scripts/04-import-borders.sh   # 6 minutes
	/kpn/scripts/05-import-wikidata.sh  # 4 minutes

(Re-)Create sql scripts:

	/kpn/scripts/06-import-sql.sh  # 4 hours

Change MIN_ZOOM to 4 and MAX_ZOOM to 12 in

	.env

Determine bounding box (data/all.dc-config.yml) and prepare for generating tiles:

	# not needed anymore?, using configuration files on /kpn/conf
	/kpn/scripts/07-generate-dc-config.sh  # 5 minutes

Adapt data/all.dc-config.yml, default zoom levels:

	cp all.dc-config.yml /kpn/conf/tiles-config-12.yml
	cp all.dc-config.yml /kpn/conf/tiles-config-13.yml
	cp all.dc-config.yml /kpn/conf/tiles-config-14.yml

Generate tiles:

	/kpn/scripts/08-generate-tiles-12.sh     # 6.5 hours

Rename data/tiles.mbtiles !!

Generate zoom level 13:    data/all.dc-config.yml !!!!

	/kpn/scripts/09-generate-tiles-13.sh     # 6 hours

Rename data/tiles.mbtiles !!

Generate zoom level 14:

	/kpn/scripts/08-generate-tiles-14.sh    # almost 2 days

Rename data/tiles.mbtiles !!

Copy tiles to kpn server, on kpn server:

	cd /kpn/tiles-install
	scp kpn-tiles:/kpn/openmaptiles/data/tiles.mbtiles.12 .
	scp kpn-tiles:/kpn/openmaptiles/data/tiles.mbtiles.13 .
	scp kpn-tiles:/kpn/openmaptiles/data/tiles.mbtiles.14 .

On kpn server:

	nohup /kpn/soft/mbutil/mb-util tiles.mbtiles.12 t12 --image_format=pbf >> /kpn/logs/mbutil-12.log 2>&1 &
	nohup /kpn/soft/mbutil/mb-util tiles.mbtiles.13 t13 --image_format=pbf >> /kpn/logs/mbutil-13.log 2>&1 &
	nohup /kpn/soft/mbutil/mb-util tiles.mbtiles.14 t14 --image_format=pbf >> /kpn/logs/mbutil-14.log 2>&1 &

	mkdir osm
	mv t12/12 osm
	mv t13/13 osm
	mv t14/14 osm


nohup rsync -r /kpn/tiles-install/osm/* /kpn/tiles/osm/ >> /kpn/logs/tiles-rsync.log 2>&1 &

	for file in */*.pbf; do mv -- "${file}" "${file/%pbf/mvt}";done


Make productive:

	somehow stop lsyncd daemon
	mv /kpn/tiles/osm dir away to backup location
	mv /kpn/tile-install/ new osm dir to /kpn/tiles

	also make same mv on kpn machine

	restart lsyncd daemon