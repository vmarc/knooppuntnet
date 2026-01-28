import { PoiDetail } from '@api/common/poi-detail';
import { OlUtil } from '@app/ol/ol-util';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Marker } from '../domain/marker';
import { OldOldLayers } from './old-old-layers';
import { OldOldMapLayer } from './old-old-map-layer';

export class PoiMarkerLayer {
  static build(poiDetail: PoiDetail): OldOldMapLayer {
    const coordinate = OlUtil.toCoordinate(poiDetail.poi.latitude, poiDetail.poi.longitude);
    const marker = Marker.create('blue', coordinate);

    const source = new VectorSource();
    const layer = new VectorLayer({
      zIndex: OldOldLayers.zIndexNetworkNodesLayer,
      source,
    });

    source.addFeature(marker);
    const name = $localize`:@@map.layer.poi-detail:Points of interest`;
    return OldOldMapLayer.build('poi-marker-layer', name, layer);
  }
}
