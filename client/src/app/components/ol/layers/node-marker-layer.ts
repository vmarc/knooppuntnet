import { NodeMapInfo } from '@api/common';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Util } from '../../shared';
import { Marker } from '../domain';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class NodeMarkerLayer {
  static build(nodeMapInfo: NodeMapInfo): MapLayer {
    const coordinate = Util.toCoordinate(
      nodeMapInfo.latitude,
      nodeMapInfo.longitude
    );
    const marker = Marker.create('blue', coordinate);

    const source = new VectorSource();
    const layer = new VectorLayer({
      zIndex: Layers.zIndexNetworkNodesLayer,
      source,
    });

    source.addFeature(marker);

    return MapLayer.simpleLayer('node-marker-layer', layer);
  }
}
