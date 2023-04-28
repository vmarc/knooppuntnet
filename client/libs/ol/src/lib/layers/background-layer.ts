import MapLibreLayer from '@geoblocks/ol-maplibre-layer';
import { Source } from 'ol/source';
import { OsmLibertyStyle } from '../style';
import { MapLayer } from './map-layer';

export class BackgroundLayer {
  static id = 'background';

  static build(): MapLayer {
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
    return new MapLayer(this.id, this.id, -Infinity, Infinity, layer);
  }
}
