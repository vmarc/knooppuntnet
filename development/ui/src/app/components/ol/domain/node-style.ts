import Circle from "ol/style/Circle";
import Fill from "ol/style/Fill";
import Stroke from "ol/style/Stroke";
import Style from "ol/style/Style";
import Text from "ol/style/Text";
import {MainStyleColors} from "./main-style-colors";

export class NodeStyle {

  public static smallNodeStyle(): Style {
    return new Style({
      image: new Circle({
        radius: 3,
        fill: new Fill({
          color: MainStyleColors.white
        }),
        stroke: new Stroke({
          color: MainStyleColors.green,
          width: 2
        })
      })
    });
  }

  public static largeNodeStyle(): Style {
    return new Style({
      image: new Circle({
        radius: 14,
        fill: new Fill({
          color: MainStyleColors.white
        }),
        stroke: new Stroke({
          color: MainStyleColors.green,
          width: 3
        })
      }),
      text: new Text({
        text: "",
        textAlign: "center",
        textBaseline: "middle",
        font: "14px Arial, Verdana, Helvetica, sans-serif",
        stroke: new Stroke({
          color: MainStyleColors.white,
          width: 5
        })
      })
    })
  }

}
