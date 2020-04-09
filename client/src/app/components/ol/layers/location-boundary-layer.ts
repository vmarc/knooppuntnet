import {GeoJSON} from "ol/format";
import BaseLayer from "ol/layer/Base";
import VectorLayer from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import {Fill} from "ol/style";
import {Stroke} from "ol/style";
import {Style} from "ol/style";
import {I18nService} from "../../../i18n/i18n.service";

export class LocationBoundaryLayer {

  constructor(private i18nService: I18nService) {
  }

  build(geoJson: string): BaseLayer {

    const features = new GeoJSON().readFeatures(geoJson, {featureProjection: "EPSG:3857"});

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
