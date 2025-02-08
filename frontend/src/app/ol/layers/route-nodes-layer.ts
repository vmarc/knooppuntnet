import { RouteNodeChange } from '@api/common/route/route-node-change';
import { OlUtil } from '@app/ol/ol-util';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Marker } from '../domain/marker';
import { OldLayers } from './old-layers';
import { OldMapLayer } from './old-map-layer';

export class RouteNodesLayer {
  static build(nodeChanges: RouteNodeChange[]): OldMapLayer {
    if (nodeChanges.length === 0) {
      return null;
    }

    const source = new VectorSource();
    nodeChanges.forEach((nodeChange) => {
      const coordinate = OlUtil.latLonToCoordinate(nodeChange);
      let color = 'blue';
      if (nodeChange.changeType === 'added') {
        color = 'green';
      } else if (nodeChange.changeType === 'removed') {
        color = 'red';
      } else if (nodeChange.changeType === 'changed') {
        color = 'orange';
      }

      const nodeMarker = Marker.create(color, coordinate);
      source.addFeature(nodeMarker);
    });
    const layer = new VectorLayer({
      zIndex: OldLayers.zIndexNetworkNodesLayer,
      source,
    });
    const name = $localize`:@@map.layer.route-nodes:Nodes`;
    return OldMapLayer.build('route-nodes-layer', name, layer);
  }
}
