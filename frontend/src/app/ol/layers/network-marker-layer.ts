import { SubsetMapNetwork } from '@api/common/subset/subset-map-network';
import { OlUtil } from '@app/ol';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { OldMapLayer } from '.';
import { Marker } from '../domain';

export class NetworkMarkerLayer {
  static readonly networkId = 'network-id';
  static readonly layer = 'layer';
  static readonly networkMarker = 'network-marker';

  build(networks: SubsetMapNetwork[]): OldMapLayer {
    const markers = networks.map((network) => {
      const coordinate = OlUtil.toCoordinate(network.center.latitude, network.center.longitude);
      const marker = Marker.create('blue', coordinate);
      marker.set(NetworkMarkerLayer.networkId, network.id.toString());
      marker.set(NetworkMarkerLayer.layer, NetworkMarkerLayer.networkMarker);
      return marker;
    });

    const source = new VectorSource();
    const layer = new VectorLayer({
      source,
    });
    markers.forEach((marker) => source.addFeature(marker));
    const name = $localize`:@@map.layer.networks:Networks`;
    return OldMapLayer.build('network-marker-layer', name, layer);
  }
}
