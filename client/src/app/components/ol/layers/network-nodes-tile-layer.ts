import {List} from "immutable";
import MVT from "ol/format/MVT";
import VectorTileLayer from "ol/layer/VectorTile";
import Map from "ol/Map";
import VectorTile from "ol/source/VectorTile";
import {createXYZ} from "ol/tilegrid";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {ZoomLevel} from "../domain/zoom-level";
import {NetworkNodesMapStyle} from "../style/network-nodes-map-style";
import {MapLayer} from "./map-layer";

export class NetworkNodesTileLayer {

  public static build(networkType: NetworkType, nodeIds: List<number>, routeIds: List<number>): MapLayer {

    const tileGrid = createXYZ({
      tileSize: 512,
      minZoom: ZoomLevel.vectorTileMinZoom,
      maxZoom: ZoomLevel.vectorTileMaxZoom
    });

    const source = new VectorTile({
      format: new MVT(),
      tileGrid: tileGrid,
      url: "/tiles/" + networkType.name + "/{z}/{x}/{y}.mvt"
    });

    const layer = new VectorTileLayer({
      source: source,
      renderMode: "image"
    });

    layer.setMinZoom(ZoomLevel.vectorTileMinZoom);
    layer.setMaxZoom(ZoomLevel.vectorTileMaxOverZoom);

    const applyMap = (map: Map) => {
      const nodeMapStyle = new NetworkNodesMapStyle(map, nodeIds, routeIds).styleFunction();
      layer.setStyle(nodeMapStyle);
    };

    return new MapLayer(`network-nodes-${networkType.name}-layer`, layer, applyMap);
  }

}
