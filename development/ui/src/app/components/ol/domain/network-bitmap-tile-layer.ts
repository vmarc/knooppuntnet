import TileLayer from 'ol/layer/Tile';
import XYZ from 'ol/source/XYZ';
import {fromLonLat} from 'ol/proj';
import {createXYZ} from 'ol/tilegrid';
import {click, pointerMove} from 'ol/events/condition';
import {Icon, Style} from 'ol/style';

import {ZoomLevel} from "./zoom-level";
import {NetworkType} from "../../../kpn/shared/network-type";

export class NetworkBitmapTileLayer {

  public static build(networkType: NetworkType) {
    return new TileLayer({
      source: new XYZ({
        minZoom: ZoomLevel.bitmapTileMinZoom,
        maxZoom: ZoomLevel.bitmapTileMaxZoom,
        url: "/tiles/" + networkType.name + "/{z}/{x}/{y}.png"
      })
    });
  }

}
