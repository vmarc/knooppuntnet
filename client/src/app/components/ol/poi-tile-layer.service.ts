import {Injectable} from "@angular/core";
import VectorTileLayer from "ol/layer/VectorTile";
import {StyleFunction} from "ol/style/Style";
import {PoiService} from "../../services/poi.service";
import {PoiStyleMap} from "./domain/poi-style-map";
import {MapLayerService} from "./map-layer.service";

@Injectable()
export class PoiTileLayerService {

  poiStyleMap: PoiStyleMap;

  constructor(private mapLayerService: MapLayerService,
              private poiService: PoiService) {
    poiService.poiConfiguration.subscribe(configuration => {
      if (configuration !== null) {
        this.poiStyleMap = new PoiStyleMap(configuration);
      }
    });
  }

  public buildLayer(): VectorTileLayer {
    const layer = this.mapLayerService.poiTileLayer();
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
