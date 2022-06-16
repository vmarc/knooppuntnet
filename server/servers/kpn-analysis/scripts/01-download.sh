cd /kpn/tile-data
wget http://download.geofabrik.de/europe/belgium-latest.osm.pbf
wget http://download.geofabrik.de/europe/netherlands-latest.osm.pbf
wget http://download.geofabrik.de/europe/germany-latest.osm.pbf
wget http://download.geofabrik.de/europe/france-latest.osm.pbf
wget http://download.geofabrik.de/europe/austria-latest.osm.pbf
wget http://download.geofabrik.de/europe/spain-latest.osm.pbf
wget http://download.geofabrik.de/europe/luxembourg-latest.osm.pbf
osmium merge \
  belgium-latest.osm.pbf \
  netherlands-latest.osm.pbf \
  germany-latest.osm.pbf \
  france-latest.osm.pbf \
  austria-latest.osm.pbf \
  spain-latest.osm.pbf \
  luxembourg-latest.osm.pbf \
  -o all.osm.pbf