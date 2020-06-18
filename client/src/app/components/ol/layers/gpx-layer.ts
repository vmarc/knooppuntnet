import VectorLayer from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import {Stroke} from "ol/style";
import {Style} from "ol/style";
import {Layers} from "./layers";

export class GpxLayer {

  build(): VectorLayer {

    const lineStyle = new Style({
      stroke: new Stroke({
        color: "rgba(255, 0, 0, 0.3)",
        lineDash: [1, 25],
        width: 15
      })
    });

    const style = {
      // "Point": not rendered for now
      "LineString": lineStyle,
      "MultiLineString": lineStyle
    };

    return new VectorLayer({
      zIndex: Layers.zIndexGpxLayer,
      source: new VectorSource({
        features: []
      }),
      style: function (feature) {
        return style[feature.getGeometry().getType()];
      }
    });
  }
}
