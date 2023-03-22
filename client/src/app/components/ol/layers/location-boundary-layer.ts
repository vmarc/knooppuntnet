import { GeoJSON } from 'ol/format';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Fill } from 'ol/style';
import { Stroke } from 'ol/style';
import { Style } from 'ol/style';
import { MapLayer } from './map-layer';

export class LocationBoundaryLayer {
  build(geoJson: string): MapLayer {
    const features = new GeoJSON().readFeatures(geoJson, {
      featureProjection: 'EPSG:3857',
    });

    const vectorSource = new VectorSource({
      features,
    });

    const locationStyle = new Style({
      stroke: new Stroke({
        color: 'rgba(255, 0, 0, 0.9)',
        width: 3,
      }),
      fill: new Fill({
        color: 'rgba(255, 0, 0, 0.05)',
      }),
    });

    const layer = new VectorLayer({
      source: vectorSource,
      style: (feature) => {
        return locationStyle;
      },
    });

    return MapLayer.simpleLayer('location-boundary', layer);
  }
}
