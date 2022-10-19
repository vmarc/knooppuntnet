import { PoiDetail } from '@app/kpn/api/common/poi-detail';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { I18nService } from '../../../i18n/i18n.service';
import { Util } from '../../shared/util';
import { Marker } from '../domain/marker';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class PoiMarkerLayer {
  constructor(private i18nService: I18nService) {}

  build(poiDetail: PoiDetail): MapLayer {
    const coordinate = Util.toCoordinate(
      poiDetail.poi.latitude,
      poiDetail.poi.longitude
    );
    const marker = Marker.create('blue', coordinate);

    const source = new VectorSource();
    const layer = new VectorLayer({
      zIndex: Layers.zIndexNetworkNodesLayer,
      source,
    });

    source.addFeature(marker);

    const layerName = this.i18nService.translation('@@map.layer.poi');
    layer.set('name', layerName);

    return new MapLayer('poi-marker-layer', layer);
  }
}
