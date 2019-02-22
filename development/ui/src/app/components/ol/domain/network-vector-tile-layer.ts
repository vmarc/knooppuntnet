import MVT from 'ol/format/MVT';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import {fromLonLat} from 'ol/proj';
import {createXYZ} from 'ol/tilegrid';
import {click, pointerMove} from 'ol/events/condition';
import Feature from 'ol/Feature';
import {Icon, Style} from 'ol/style';

import {ZoomLevel} from "./zoom-level";
import {NetworkType} from "../../../kpn/shared/network-type";

export class NetworkVectorTileLayer {

  public static build(networkType: NetworkType): VectorTileLayer {

    const urlFunction = function (tileCoord, pixelRatio, projection) {
      const zIn = tileCoord[0];
      const xIn = tileCoord[1];
      const yIn = tileCoord[2];

      const z = zIn >= ZoomLevel.vectorTileMaxZoom ? ZoomLevel.vectorTileMaxZoom : zIn;
      const x = xIn;
      const y = -yIn - 1;
      return "/tiles/" + networkType.name + "/" + z + "/" + x + "/" + y + ".mvt"
    };

    const tileGrid = createXYZ({
      // minZoom: ZoomLevel.vectorTileMinZoom
      // maxZoom: ZoomLevel.vectorTileMaxOverZoom
    });

    const source = new VectorTile({
      format: new MVT({
        featureClass: Feature // this is important to avoid error upon first selection in the map
      }),
      tileGrid: tileGrid,
      tileUrlFunction: urlFunction
    });

    const layer = new VectorTileLayer({
      source: source
    });

    return layer;

  }

}
