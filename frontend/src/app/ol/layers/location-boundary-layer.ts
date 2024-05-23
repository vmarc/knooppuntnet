import { GeoJSON } from 'ol/format';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Stroke } from 'ol/style';
import { Style } from 'ol/style';
import { MapLayer } from './map-layer';

export class LocationBoundaryLayer {
  static id = 'location-boundary';

  static build(geoJson: string): MapLayer {
    const features = new GeoJSON().readFeatures(geoJson, {
      featureProjection: 'EPSG:3857',
    });

    console.log(['all features', features]);

    const vectorSource = new VectorSource({
      features,
    });

    const locationStyle = new Style({
      stroke: new Stroke({
        color: 'rgba(255, 0, 0, 0.9)',
        width: 3,
      }),
    });

    const layer = new VectorLayer({
      source: vectorSource,
      style: (feature) => {
        return locationStyle;
      },
    });

    const name = $localize`:@@map.layer.boundary:Boundary`;
    return MapLayer.build('location-boundary', name, layer);
  }
}
