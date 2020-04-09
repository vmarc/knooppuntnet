import {Injectable} from "@angular/core";
import stylefunction from "ol-mapbox-style/dist/stylefunction";
import Collection from "ol/Collection";
import {defaults as defaultControls} from "ol/control";
import {Attribution} from "ol/control";
import {ScaleLine} from "ol/control";
import {FullScreen} from "ol/control";
import Control from "ol/control/Control";
import {GeoJSON} from "ol/format";
import MVT from "ol/format/MVT";
import BaseLayer from "ol/layer/Base";
import VectorLayer from "ol/layer/Vector";
import VectorTileLayer from "ol/layer/VectorTile";
import VectorSource from "ol/source/Vector";
import VectorTile from "ol/source/VectorTile";
import {Fill} from "ol/style";
import {Stroke} from "ol/style";
import {Style} from "ol/style";
import {createXYZ} from "ol/tilegrid";
import {I18nService} from "../../i18n/i18n.service";
import {osmStyle} from "./domain/style";

@Injectable()
export class MapLayerService {

  constructor(private i18nService: I18nService) {
  }

  controls(): Collection<Control> {
    const fullScreen = new FullScreen();
    const scaleLine = new ScaleLine();
    const attribution = new Attribution({
      collapsible: false
    });
    return defaultControls({attribution: false}).extend([fullScreen, scaleLine, attribution]);
  }

  osmLayer(): BaseLayer {

    const tileGrid = createXYZ({
      tileSize: 512,
      maxZoom: 14
    });

    const source = new VectorTile({
      format: new MVT(),
      tileGrid: tileGrid,
      url: "/tiles/osm/{z}/{x}/{y}.pbf",
      attributions: [
        "<a href=\"https://www.openmaptiles.org/\" target=\"_blank\">&copy; OpenMapTiles</a>",
        "<a href=\"https://www.openstreetmap.org/copyright\" target=\"_blank\">&copy; OpenStreetMap contributors</a>"
      ]
    });

    const layer = new VectorTileLayer({
      source: source,
      renderMode: "image"
    });

    stylefunction(layer, osmStyle, "openmaptiles");
    const osmLayerName = this.i18nService.translation("@@map.layer.osm");
    layer.set("name", osmLayerName);
    return layer;
  }

  locationBoundaryLayer(geoJson: string): BaseLayer {

    const features = (new GeoJSON()).readFeatures(geoJson, {featureProjection: "EPSG:3857"});

    const vectorSource = new VectorSource({
      features: features
    });

    const locationStyle = new Style({
        stroke: new Stroke({
          color: "rgba(255, 0, 0, 0.9)",
          width: 3
        }),
        fill: new Fill({
          color: "rgba(255, 0, 0, 0.05)"
        })
      }
    );

    const styleFunction = function (feature) {
      return locationStyle;
    };

    const layer = new VectorLayer({
      source: vectorSource,
      style: styleFunction
    });

    const geoJsonLayerName = this.i18nService.translation("@@map.layer.boundary");
    layer.set("name", geoJsonLayerName);
    return layer;
  }

}
