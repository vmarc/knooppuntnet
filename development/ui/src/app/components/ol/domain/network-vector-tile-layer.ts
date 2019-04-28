import Feature from "ol/Feature";
import MVT from "ol/format/MVT";
import VectorTileLayer from "ol/layer/VectorTile";
import VectorTile from "ol/source/VectorTile";
import {createXYZ} from "ol/tilegrid";
import {NetworkType} from "../../../kpn/shared/network-type";

export class NetworkVectorTileLayer {

  public static build(networkType: NetworkType): VectorTileLayer {

    const tileGrid = createXYZ({
      tileSize: 512,
      minZoom: 12,
      maxZoom: 15
    });

    const source = new VectorTile({
      format: new MVT({
        featureClass: Feature // this is important to avoid error upon first selection in the map
      }),
      tileGrid: tileGrid,
      url: "/tiles/" + networkType.name + "/{z}/{x}/{y}.mvt"
    });

    const layer = new VectorTileLayer({
      source: source
    });

    return layer;

  }

}
