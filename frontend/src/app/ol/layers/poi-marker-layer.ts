import { PoiDetail } from '@api/common';
import { OlUtil } from '@app/ol';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Marker } from '../domain';
import { OldLayers } from './old-layers';
import { MapLayer } from './map-layer';

export class PoiMarkerLayer {
  static build(poiDetail: PoiDetail): MapLayer {
    const coordinate = OlUtil.toCoordinate(poiDetail.poi.latitude, poiDetail.poi.longitude);
    const marker = Marker.create('blue', coordinate);

    const source = new VectorSource();
    const layer = new VectorLayer({
      zIndex: OldLayers.zIndexNetworkNodesLayer,
      source,
    });

    source.addFeature(marker);
    const name = $localize`:@@map.layer.poi-detail:Points of interest`;
    return MapLayer.build('poi-marker-layer', name, layer);
  }
}
