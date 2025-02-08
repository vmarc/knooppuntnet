import { NetworkMapNode } from '@api/common/network/network-map-node';
import { OlUtil } from '@app/ol/ol-util';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Marker } from '../domain/marker';
import { OldLayers } from './old-layers';
import { OldMapLayer } from './old-map-layer';

export class NetworkNodesMarkerLayer {
  static build(nodes: NetworkMapNode[]): OldMapLayer {
    const markers = nodes.map((node) => {
      const color = node.roleConnection ? 'orange' : 'blue';
      const coordinate = OlUtil.toCoordinate(node.latitude, node.longitude);
      const marker = Marker.create(color, coordinate);
      marker.set('id', node.id.toString());
      marker.set('name', node.name);
      marker.set('layer', 'node-marker');
      return marker;
    });

    const source = new VectorSource();
    const layer = new VectorLayer({
      zIndex: OldLayers.zIndexNetworkNodesLayer,
      source,
    });

    markers.forEach((marker) => source.addFeature(marker));

    const name = $localize`:@@map.layer.nodes:Nodes`;
    return OldMapLayer.build('network-node-markers-layer', name, layer);
  }
}
