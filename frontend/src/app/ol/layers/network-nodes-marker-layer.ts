import { NetworkMapNode } from '@api/common/network';
import { OlUtil } from '@app/ol';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Marker } from '../domain';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class NetworkNodesMarkerLayer {
  static build(nodes: NetworkMapNode[]): MapLayer {
    const markers = nodes.map((node) => {
      const color = node.roleConnection ? 'green' : 'blue';
      const coordinate = OlUtil.toCoordinate(node.latitude, node.longitude);
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

    const name = $localize`:@@map.layer.nodes:Nodes`;
    return MapLayer.build('network-node-markers-layer', name, layer);
  }
}
