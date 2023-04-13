import { SubsetMapNetwork } from '@api/common/subset';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { MapLayer } from '.';
import { Util } from '../../shared';
import { Marker } from '../domain';

export class NetworkMarkerLayer {
  static readonly networkId = 'network-id';
  static readonly layer = 'layer';
  static readonly networkMarker = 'network-marker';

  build(networks: SubsetMapNetwork[]): MapLayer {
    const markers = networks.map((network) => {
      const coordinate = Util.toCoordinate(
        network.center.latitude,
        network.center.longitude
      );
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
    return MapLayer.simpleLayer('network-marker-layer', layer);
  }
}
