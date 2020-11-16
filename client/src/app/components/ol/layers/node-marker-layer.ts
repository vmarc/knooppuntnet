import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import {I18nService} from '../../../i18n/i18n.service';
import {NodeMapInfo} from '../../../kpn/api/common/node-map-info';
import {Util} from '../../shared/util';
import {Marker} from '../domain/marker';
import {MapLayer} from './map-layer';
import {Layers} from './layers';

export class NodeMarkerLayer {

  constructor(private i18nService: I18nService) {
  }

  build(nodeMapInfo: NodeMapInfo): MapLayer {

    const coordinate = Util.toCoordinate(nodeMapInfo.latitude, nodeMapInfo.longitude);
    const marker = Marker.create('blue', coordinate);

    const source = new VectorSource();
    const layer = new VectorLayer({
      zIndex: Layers.zIndexNetworkNodesLayer,
      source
    });

    source.addFeature(marker);

    const layerName = this.i18nService.translation('@@map.layer.node');
    layer.set('name', layerName);

    return new MapLayer('node-marker-layer', layer);
  }

}
