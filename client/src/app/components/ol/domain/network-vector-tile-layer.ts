import {FeatureClass} from "ol/Feature";
import Feature from "ol/Feature";
import MVT from "ol/format/MVT";
import Geometry from "ol/geom/Geometry";
import VectorTileLayer from "ol/layer/VectorTile";
import RenderFeature from "ol/render/Feature";
import VectorTile from "ol/source/VectorTile";
import {createXYZ} from "ol/tilegrid";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {ZoomLevel} from "./zoom-level";

export class NetworkVectorTileLayer {

  public static build(networkType: NetworkType): VectorTileLayer {

    const tileGrid = createXYZ({
      tileSize: 512,
      minZoom: ZoomLevel.vectorTileMinZoom,
      maxZoom: ZoomLevel.vectorTileMaxZoom
    });

    const xxx = new Feature<Geometry>();
    const featureClass: FeatureClass = xxx;

    const source = new VectorTile({
      format: new MVT(
      //   {
      //   featureClass: Feature<Geometry> as FeatureClass // this is important to avoid error upon first selection in the map
      // }
      ),
      tileGrid: tileGrid,
      url: "/tiles/" + networkType.name + "/{z}/{x}/{y}.mvt"
    });

    return new VectorTileLayer({
      source: source,
      renderMode: "image"
    });
  }

}
