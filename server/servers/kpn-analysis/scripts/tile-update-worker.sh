rm /kpn/tile-data/*.pbf
rm -rf /kpn/openmaptiles/data/*
rm -rf /kpn/tiles-install/*
cd /kpn/tile-data
wget --no-verbose http://download.geofabrik.de/europe/belgium-latest.osm.pbf
wget --no-verbose http://download.geofabrik.de/europe/netherlands-latest.osm.pbf
wget --no-verbose http://download.geofabrik.de/europe/germany-latest.osm.pbf
wget --no-verbose http://download.geofabrik.de/europe/france-latest.osm.pbf
wget --no-verbose http://download.geofabrik.de/europe/austria-latest.osm.pbf
wget --no-verbose http://download.geofabrik.de/europe/spain-latest.osm.pbf
wget --no-verbose http://download.geofabrik.de/europe/luxembourg-latest.osm.pbf
wget --no-verbose http://download.geofabrik.de/europe/denmark-latest.osm.pbf
osmium merge \
  belgium-latest.osm.pbf \
  netherlands-latest.osm.pbf \
  germany-latest.osm.pbf \
  france-latest.osm.pbf \
  austria-latest.osm.pbf \
  spain-latest.osm.pbf \
  luxembourg-latest.osm.pbf \
  denmark-latest.osm.pbf \
  -o all.osm.pbf
mv /kpn/tile-data/all.osm.pbf /kpn/openmaptiles/data/all.osm.pbf
cd /kpn/openmaptiles
make destroy-db
make start-db import-data import-osm import-borders import-wikidata
make clean
make
make import-sql
make generate-tiles
/kpn/soft/mbutil/mb-util /kpn/openmaptiles/data/tiles.mbtiles osm --image_format=pbf >> /kpn/logs/mbutil.log 2>&1
echo file count /kpn/tiles-install/osm
find /kpn/tiles-install/osm -type f | wc -l
echo file count /kpn/tiles/osm
find /kpn/tiles/osm -type f | wc -l
echo done