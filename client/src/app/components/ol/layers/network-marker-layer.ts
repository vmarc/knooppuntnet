import {List} from "immutable";
import VectorLayer from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import {I18nService} from "../../../i18n/i18n.service";
import {SubsetMapNetwork} from "../../../kpn/api/common/subset/subset-map-network";
import {Util} from "../../shared/util";
import {Marker} from "../domain/marker";
import {MapLayer} from "./map-layer";

export class NetworkMarkerLayer {

  static readonly networkId = "network-id";
  static readonly layer = "layer";
  static readonly networkMarker = "network-marker";

  constructor(private i18nService: I18nService) {
  }

  build(networks: List<SubsetMapNetwork>): MapLayer {

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
    const name = this.i18nService.translation("@@map.layer.networks");
    layer.set("name", name);

    markers.forEach(marker => source.addFeature(marker));
    return new MapLayer(layer);
  }

}
