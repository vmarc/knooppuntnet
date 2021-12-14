# Locations

## Description

## Countries

### Netherlands

|location id|location type|
|---|---|
|nl|country|
|nl/1/ref|province|
|nl/2/ref:gemeentecode|municipality|

### Belgium

|location id|location type|
|---|---|
|be|country|
|be/1/ref:INS|province or Brussels-Capital region|
|be/2/ref:INS|municipality|

ref:INS contais the INS (Dutch NIS) code, see https://en.wikipedia.org/wiki/NIS_code

region and arondissement levels are skipped


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

Most of the location data is not read directly from the OpenStreetMap database, but from the osm-boundaries website, because this site contains boundaries that includes water rather than following the coastlines. This is better for performance.

The only exception is the France CDC's, the are read from OverpassApi.

Procedure:

- Go to [osm-boundaries.com](https://osm-boundaries.com/).
- Click the "download" button at the bottom.
- Select the admin level
- Select "Include water"
- Select "Include all tags"
- Select "Simplify No"
- Copy the curl command
- execute the curl command
- rename the resulting file to <country>-level-<level-number>.geojson.gz


### Netherlands

- /kpn/locations/osm-boundaries-2021-11-01/nl-level-3.geojson.gz: country boundaries
- /kpn/locations/osm-boundaries-2021-11-01/nl-level-4.geojson.gz: province boundaries
- /kpn/locations/osm-boundaries-2021-11-01/nl-level-8.geojson.gz: municipality boundaries


### Belgium

- /kpn/locations/osm-boundaries-2021-11-01/be-level-2.geojson.gz: country boundaries
- /kpn/locations/osm-boundaries-2021-11-01/be-level-4.geojson.gz: region boundaries
- /kpn/locations/osm-boundaries-2021-11-01/be-level-6.geojson.gz: province boundaries
- /kpn/locations/osm-boundaries-2021-11-01/be-level-8.geojson.gz: municipality boundaries


### Germany

- /kpn/locations/osm-boundaries-2021-11-01/de-level-2.geojson.gz: country boundaries
- /kpn/locations/osm-boundaries-2021-11-01/de-level-4.geojson.gz: federal state boundaries (Bundesland)
- /kpn/locations/osm-boundaries-2021-11-01/de-level-5.geojson.gz: state district boundaries (Regierungsbezirk)
- /kpn/locations/osm-boundaries-2021-11-01/de-level-6.geojson.gz: county boundaries (Landkreis / Kreis / kreisfreie Stadt / Stadtkreis)


### France

- /kpn/locations/fr-level-3.geojson.gz: (region boundaries) used for country boundary 
- /kpn/locations/fr-level-6.geojson.gz: department boundaries
- /kpn/locations/fr-level-8.geojson.gz: municipality boundaries

France CDC boundaries
- Overpass: relation['local_authority:FR'='CC'];out ids;


### Austria

- /kpn/locations/osm-boundaries-2021-11-01/at-level-2.geojson.gz: country boundaries
- /kpn/locations/osm-boundaries-2021-11-01/at-level-4.geojson.gz: region boundaries
- /kpn/locations/osm-boundaries-2021-11-01/at-level-6.geojson.gz: municipality boundaries


### Spain

- /kpn/locations/osm-boundaries-2021-11-01/es-level-2.geojson.gz: country boundaries
- /kpn/locations/osm-boundaries-2021-11-01/es-level-4.geojson.gz: region boundaries
- /kpn/locations/osm-boundaries-2021-11-01/es-level-6.geojson.gz: municipality boundaries
