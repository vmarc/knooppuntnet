import { NodeMapInfo } from '@api/common';
import { OlUtil } from '@app/components/ol';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { Marker } from '../domain';
import { Layers } from './layers';
import { MapLayer } from './map-layer';

export class NodeMarkerLayer {
  static build(nodeMapInfo: NodeMapInfo): MapLayer {
    const coordinate = OlUtil.toCoordinate(
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
