import {Color} from "ol/color";
import Circle from "ol/style/Circle";
import Fill from "ol/style/Fill";
import Stroke from "ol/style/Stroke";
import Style from "ol/style/Style";
import Text from "ol/style/Text";
import {MainStyleColors} from "./main-style-colors";

export class NodeStyle {

  public static smallNodeStyle(): Style {
    return this.buildSmallNodeStyle(MainStyleColors.green);
  }

  public static smallNodeStyleGray(): Style {
    return this.buildSmallNodeStyle(MainStyleColors.gray);
  }

  public static largeNodeStyle(): Style {
    return this.buildLargeNodeStyleGray(MainStyleColors.green);
  }

  public static largeNodeStyleGray(): Style {
    return this.buildLargeNodeStyleGray(MainStyleColors.gray);
  }

  private static buildSmallNodeStyle(color: Color): Style {
    return new Style({
      image: new Circle({
        radius: 3,
        fill: new Fill({
          color: MainStyleColors.white
        }),
        stroke: new Stroke({
          color: color,
          width: 2
        })
      })
    });
  }

  private static buildLargeNodeStyleGray(color: Color): Style {
    return new Style({
      image: new Circle({
        radius: 14,
        fill: new Fill({
          color: MainStyleColors.white
        }),
        stroke: new Stroke({
          color: color,
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
    });
  }
}
