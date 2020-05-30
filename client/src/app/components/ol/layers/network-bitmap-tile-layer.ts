import TileLayer from "ol/layer/Tile";
import XYZ from "ol/source/XYZ";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {ZoomLevel} from "../domain/zoom-level";
import {MapLayer} from "./map-layer";

export class NetworkBitmapTileLayer {

  public static build(networkType: NetworkType): MapLayer {
    return new MapLayer(
      `network-bitmap-tiles-${networkType.name}-layer`,
      new TileLayer({
        source: new XYZ({
          minZoom: ZoomLevel.bitmapTileMinZoom,
          maxZoom: ZoomLevel.bitmapTileMaxZoom,
          url: "/tiles/" + networkType.name + "/{z}/{x}/{y}.png"
        })
      })
    );
  }

}
