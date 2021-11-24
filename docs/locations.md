# Locations

## Description


## Countries

### Netherlands

### Belgium

### Germany

### France

|location id|location type|
|---|---|
|fr|country|
|fr/1/insee-code|department|
|fr/2/siren-code|CDC|
|fr/3/insee-code|commune|

Tree structure:
  - departement
    - CDC or isolated communes
      - communes



### Austria

### Spain


## Collecting location data


France country boundaries: /kpn/locations/fr-regions.geojson.gz

curl --remote-name --remote-header-name --location --max-redirs -1 "https://osm-boundaries.com/Download/Submit?apiKey=XXX&db=osm20210830&osmIds=-2202162&recursive&minAdminLevel=3&maxAdminLevel=3&format=GeoJSON&srid=4326&includeAllTags"


France department boundaries: /kpn/locations/fr-departments.geojson.gz

curl --remote-name --remote-header-name --location --max-redirs -1 "https://osm-boundaries.com/Download/Submit?apiKey=XXX&db=osm20210830&osmIds=-2202162&recursive&minAdminLevel=6&maxAdminLevel=6&format=GeoJSON&srid=4326&includeAllTags"


France municipality boundaries: /kpn/locations/fr-communes.geojson.gz

curl --remote-name --remote-header-name --location --max-redirs -1 "https://osm-boundaries.com/Download/Submit?apiKey=XXX&db=osm20210830&osmIds=-2202162&recursive&minAdminLevel=8&maxAdminLevel=8&format=GeoJSON&srid=4326&includeAllTags"

France CDC boundaries

  Overpass: relation['local_authority:FR'='CC'];out ids;

