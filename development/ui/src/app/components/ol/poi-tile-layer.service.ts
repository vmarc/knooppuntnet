import {Injectable} from "@angular/core";
import {PoiService} from "../../poi.service";
import MVT from 'ol/format/MVT';
import VectorTileLayer from 'ol/layer/VectorTile';
import VectorTile from 'ol/source/VectorTile';
import {fromLonLat} from 'ol/proj';
import {createXYZ} from 'ol/tilegrid';
import {click, pointerMove} from 'ol/events/condition';
import Feature from 'ol/Feature';
import {Icon, Style} from 'ol/style';
import {ZoomLevel} from "./domain/zoom-level";
import {PoiStyleMap} from "./domain/poi-style-map";

@Injectable()
export class PoiTileLayerService {

  poiStyleMap: PoiStyleMap;

  constructor(private poiService: PoiService) {
    poiService.poiConfiguration.subscribe(configuration => {
      if (configuration !== null) {
        this.poiStyleMap = new PoiStyleMap(configuration)
      }
    });
  }

  public buildLayer(): VectorTileLayer {

    const urlFunction = function (tileCoord, pixelRatio, projection) {
      const zIn = tileCoord[0];
      const xIn = tileCoord[1];
      const yIn = tileCoord[2];

      const z = zIn >= ZoomLevel.vectorTileMaxZoom ? ZoomLevel.vectorTileMaxZoom : zIn;
      const x = xIn;
      const y = -yIn - 1;
      return "/tiles/poi/" + z + "/" + x + "/" + y + ".mvt"
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

    layer.setStyle(this.poiStyleFunction());

    this.poiService.changed.subscribe(changed => layer.changed());

    return layer;
  }

  private poiStyleFunction() {
    return (feature, resolution) => {
      if (this.poiStyleMap) {
        const layer = feature.get("layer");
        if (layer != null) {
          if (this.poiService.isPoiActive(layer)) {
            const style = this.poiStyleMap.get(layer);
            if (style != null) {
              return [style];
            }
          }
        }
      }
      return null;
    };
  }

}
