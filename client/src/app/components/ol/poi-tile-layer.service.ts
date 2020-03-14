import {Injectable} from "@angular/core";
import MVT from "ol/format/MVT";
import VectorTileLayer from "ol/layer/VectorTile";
import VectorTile from "ol/source/VectorTile";
import {StyleFunction} from "ol/style/Style";
import {createXYZ} from "ol/tilegrid";
import {PoiService} from "../../services/poi.service";
import {PoiStyleMap} from "./domain/poi-style-map";
import {ZoomLevel} from "./domain/zoom-level";

@Injectable()
export class PoiTileLayerService {

  poiStyleMap: PoiStyleMap;

  constructor(private poiService: PoiService) {
    poiService.poiConfiguration.subscribe(configuration => {
      if (configuration !== null) {
        this.poiStyleMap = new PoiStyleMap(configuration);
      }
    });
  }

  public buildLayer(): VectorTileLayer {

    const tileGrid = createXYZ({
      tileSize: 512,
      minZoom: ZoomLevel.poiTileMinZoom,
      maxZoom: ZoomLevel.poiTileMaxZoom
    });

    const source = new VectorTile({
      format: new MVT(),
      tileGrid: tileGrid,
      url: "/tiles/poi/{z}/{x}/{y}.mvt"
    });

    const layer = new VectorTileLayer({
      source: source,
      renderMode: "image"
    });

    layer.setStyle(this.poiStyleFunction());

    this.poiService.changed.subscribe(() => layer.changed());

    return layer;
  }

  private poiStyleFunction(): StyleFunction {
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
