import { GeoJSON } from 'ol/format';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Style } from 'ol/style';
import Fill from 'ol/style/Fill';
import Stroke from 'ol/style/Stroke';
import { OldMapLayer } from './old-map-layer';

export class LocationBoundaryLayer {
  static id = 'location-boundary';

  static build(geoJson: string): OldMapLayer {
    const features = new GeoJSON().readFeatures(geoJson, {
      featureProjection: 'EPSG:3857',
    });
    const vectorSource = new VectorSource({
      features,
    });

    const locationStyle = new Style({
      stroke: new Stroke({
        color: 'rgba(0, 0, 255, 0.5)',
        width: 1,
      }),
      fill: new Fill({
        color: 'rgba(0, 0, 255, 0.1)',
      }),
    });

    const layer = new VectorLayer({
      source: vectorSource,
      style: (feature) => {
        return locationStyle;
      },
    });

    const name = $localize`:@@map.layer.boundary:Boundary`;
    return OldMapLayer.build('location-boundary', name, layer);
  }

  static build2(geoJson: string): OldMapLayer {
    const features = new GeoJSON().readFeatures(geoJson, {
      featureProjection: 'EPSG:3857',
    });
    const vectorSource = new VectorSource({
      features,
    });

    const locationStyle = new Style({
      stroke: new Stroke({
        color: 'rgba(255, 0, 255, 0.8)',
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
    return OldMapLayer.build('location-boundary-2', name, layer);
  }
}
