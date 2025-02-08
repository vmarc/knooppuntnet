import { NodeMapInfo } from '@api/common/node-map-info';
import { OlUtil } from '@app/ol/ol-util';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Marker } from '../domain/marker';
import { OldLayers } from './old-layers';
import { OldMapLayer } from './old-map-layer';

export class NodeMarkerLayer {
  static build(nodeMapInfo: NodeMapInfo): OldMapLayer {
    const coordinate = OlUtil.toCoordinate(nodeMapInfo.latitude, nodeMapInfo.longitude);
    const marker = Marker.create('blue', coordinate);

    const source = new VectorSource();
    const layer = new VectorLayer({
      zIndex: OldLayers.zIndexNetworkNodesLayer,
      source,
    });

    source.addFeature(marker);

    const name = $localize`:@@map.layer.node:Node`;
    return OldMapLayer.build('node-marker-layer', name, layer);
  }
}
