import { MapboxVectorLayer } from 'ol-mapbox-style';
import { OldOldMapLayer } from './old-old-map-layer';

export class OldOldBackgroundLayer {
  static readonly id = 'background';

  static build(): OldOldMapLayer {
    const osmAttribution =
      '&#169; <a href="https://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap</a> contributors';
    const openMapTilesAttribution =
      '&#169; <a href="https://www.openmaptiles.org/" target="_blank">OpenMapTiles</a>';

    const layer = new MapboxVectorLayer({
      styleUrl: '/assets/osm-liberty-style.json',
    });

    layer.getSource().setAttributions([osmAttribution, openMapTilesAttribution]);
    const name = $localize`:@@map.layer.background:Background`;
    return new OldOldMapLayer(this.id, name, -Infinity, Infinity, 'vector', layer);
  }
}
