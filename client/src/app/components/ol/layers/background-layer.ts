// import { Map as MapLibreMap } from 'maplibre-gl';
import { OsmLibertyStyle } from '../style/osm-liberty-style';
import { MapLayer } from './map-layer';
import MapLibreLayer from '@geoblocks/ol-maplibre-layer';
import { Source } from 'ol/source';

export class BackgroundLayer {
  build(): MapLayer {
    const osmAttribution =
      '&#169; <a href="https://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap</a> contributors';
    const openMapTilesAttribution =
      '&#169; <a href="https://www.openmaptiles.org/" target="_blank">OpenMapTiles</a>';

    const layer = new MapLibreLayer({
      opacity: 0.7,
      source: new Source({
        attributions: [openMapTilesAttribution, osmAttribution],
      }),
      maplibreOptions: {
        style: OsmLibertyStyle.osmLibertyStyle,
      },
    });
    return new MapLayer('background', 'background', -Infinity, Infinity, layer);
  }
}
