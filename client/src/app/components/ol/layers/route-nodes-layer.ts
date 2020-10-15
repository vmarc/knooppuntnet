import {List} from "immutable";
import BaseLayer from "ol/layer/Base";
import VectorLayer from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import {I18nService} from "../../../i18n/i18n.service";
import {RawNode} from "../../../kpn/api/common/data/raw/raw-node";
import {Util} from "../../shared/util";
import {Marker} from "../domain/marker";
import {MapLayer} from "./map-layer";
import {Layers} from "./layers";

export class RouteNodesLayer {

  constructor(private i18nService: I18nService) {
  }

  build(nodes: List<RawNode>): MapLayer {

    if (nodes.isEmpty()) {
      return null;
    }

    const source = new VectorSource();
    nodes.forEach(node => {
      const after = Util.latLonToCoordinate(node);
      const nodeMarker = Marker.create("blue", after);
      source.addFeature(nodeMarker);
    });
    const layer = new VectorLayer({
      zIndex: Layers.zIndexNetworkNodesLayer,
      source: source
    });
    layer.set("name", this.i18nService.translation("@@map.layer.nodes"));
    return new MapLayer("route-nodes-layer", layer);
  }

}
