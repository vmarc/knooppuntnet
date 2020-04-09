import MVT from "ol/format/MVT";
import VectorTileLayer from "ol/layer/VectorTile";
import VectorTile from "ol/source/VectorTile";
import {createXYZ} from "ol/tilegrid";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {ZoomLevel} from "../domain/zoom-level";

export class NetworkVectorTileLayer {

  public static build(networkType: NetworkType): VectorTileLayer {

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

    return new VectorTileLayer({
      source: source,
      renderMode: "image"
    });
  }

}
