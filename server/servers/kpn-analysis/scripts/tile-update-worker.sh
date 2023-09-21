cd /kpn/openmaptiles
make destroy-db
cd /kpn/tile-data
rm *.pbf
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
cd /kpn/openmaptiles/data
rm -rf *
mv /kpn/tile-data/all.osm.pbf .
cd /kpn/openmaptiles
make start-db import-data import-osm import-borders import-wikidata
make clean
make
make import-sql
make generate-tiles
echo done