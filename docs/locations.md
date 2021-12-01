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

Most of the location data is not read directly from the OpenStreetMap database, but from the osm-boundaries website,
because this site contains boundaries that includes water rather than following the coastlines. This is better for
performance.

The only exception is the France CDC's, the are read from OverpassApi.

Procedure:

- Go to [osm-boundaries.com](https://osm-boundaries.com/).
- Click the "download" button at the bottom.
- Select the admin level
- Select "Include water"
- Copy the curl command
- execute the curl command
- rename the resulting file to <country>-level-<level-number>.geojson.gz

### Netherlands

- /kpn/locations/nl-level-3.geojson.gz: country boundaries
- /kpn/locations/nl-level-4.geojson.gz: province boundaries
- /kpn/locations/nl-level-8.geojson.gz: municipality boundaries

### Belgium

- /kpn/locations/be-level-2.geojson.gz: country boundaries
- /kpn/locations/be-level-4.geojson.gz: region boundaries
- /kpn/locations/be-level-6.geojson.gz: province boundaries
- /kpn/locations/be-level-8.geojson.gz: municipality boundaries


### Germany

de/2/Germany", Seq(2, 4, 5, 6))

### France

France country boundaries: /kpn/locations/fr-regions.geojson.gz

curl --remote-name --remote-header-name --location --max-redirs
-1 "https://osm-boundaries.com/Download/Submit?apiKey=XXX&db=osm20210830&osmIds=-2202162&recursive&minAdminLevel=3&maxAdminLevel=3&format=GeoJSON&srid=4326&includeAllTags"

France department boundaries: /kpn/locations/fr-departments.geojson.gz

curl --remote-name --remote-header-name --location --max-redirs
-1 "https://osm-boundaries.com/Download/Submit?apiKey=XXX&db=osm20210830&osmIds=-2202162&recursive&minAdminLevel=6&maxAdminLevel=6&format=GeoJSON&srid=4326&includeAllTags"

France municipality boundaries: /kpn/locations/fr-communes.geojson.gz

curl --remote-name --remote-header-name --location --max-redirs
-1 "https://osm-boundaries.com/Download/Submit?apiKey=XXX&db=osm20210830&osmIds=-2202162&recursive&minAdminLevel=8&maxAdminLevel=8&format=GeoJSON&srid=4326&includeAllTags"

France CDC boundaries

Overpass: relation['local_authority:FR'='CC'];out ids;

### Austria

"at/2/Austria", Seq(2, 4, 6))

### Spain

"es/2/Spain", Seq(2, 4, 6))
