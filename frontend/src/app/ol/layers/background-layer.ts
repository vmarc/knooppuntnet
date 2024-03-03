import { MapboxVectorLayer } from 'ol-mapbox-style';
import { MapLayer } from './map-layer';

export class BackgroundLayer {
  static readonly id = 'background';

  static build(): MapLayer {
    const osmAttribution =
      '&#169; <a href="https://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap</a> contributors';
    const openMapTilesAttribution =
      '&#169; <a href="https://www.openmaptiles.org/" target="_blank">OpenMapTiles</a>';

    const layer = new MapboxVectorLayer({
      styleUrl: '/assets/osm-liberty-style.json',
    });

    layer.getSource().setAttributions([osmAttribution, openMapTilesAttribution]);
    const name = $localize`:@@map.layer.background:Background`;
    return new MapLayer(this.id, name, -Infinity, Infinity, 'vector', layer);
  }
}
