import {List} from "immutable";
import VectorLayer from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import {NetworkAttributes} from "../../../kpn/api/common/network/network-attributes";
import {Util} from "../../shared/util";
import {Marker} from "../domain/marker";

export class NetworkMarkerLayer {

  static readonly networkId = "network-id";
  static readonly layer = "layer";
  static readonly networkMarker = "network-marker";

  build(networks: List<NetworkAttributes>) {

    const markers = networks.map(network => {
      const coordinate = Util.toCoordinate(network.center.latitude, network.center.longitude);
      const marker = Marker.create("blue", coordinate);
      marker.set(NetworkMarkerLayer.networkId, network.id.toString());
      marker.set(NetworkMarkerLayer.layer, NetworkMarkerLayer.networkMarker);
      return marker;
    });

    const source = new VectorSource();
    const layer = new VectorLayer({
      source: source
    });

    markers.forEach(marker => source.addFeature(marker));
    return layer;
  }

}
