import { RawNode } from '@api/common/data/raw';
import { OlUtil } from '@app/components/ol';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Marker } from '../domain';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class RouteNodesLayer {
  static id = 'route-nodes-layer';

  static build(nodes: RawNode[]): MapLayer {
    if (nodes.length === 0) {
      return null;
    }

    const source = new VectorSource();
    nodes.forEach((node) => {
      const after = OlUtil.latLonToCoordinate(node);
      const nodeMarker = Marker.create('blue', after);
      source.addFeature(nodeMarker);
    });
    const layer = new VectorLayer({
      zIndex: Layers.zIndexNetworkNodesLayer,
      source,
    });
    return MapLayer.simpleLayer(this.id, layer);
  }
}
