import { NetworkMapNode } from '@api/common/network/network-map-node';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Util } from '../../shared/util';
import { Marker } from '../domain/marker';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class NetworkNodesMarkerLayer {
  build(nodes: NetworkMapNode[]): MapLayer {
    const markers = nodes.map((node) => {
      const color = node.roleConnection ? 'green' : 'blue';
      const coordinate = Util.toCoordinate(node.latitude, node.longitude);
      const marker = Marker.create(color, coordinate);
      marker.set('id', node.id.toString());
      marker.set('name', node.name);
      marker.set('layer', 'node-marker');
      return marker;
    });

    const source = new VectorSource();
    const layer = new VectorLayer({
      zIndex: Layers.zIndexNetworkNodesLayer,
      source,
    });

    markers.forEach((marker) => source.addFeature(marker));
    return MapLayer.simpleLayer('network-node-markers-layer', layer);
  }
}
