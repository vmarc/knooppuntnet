import {Color} from "ol/color";
import Stroke from "ol/style/Stroke";
import Style from "ol/style/Style";
import {MainStyleColors} from "./main-style-colors";

export class RouteStyle {

  private defaultRouteStyle = this.initRouteStyle();

  style(color: Color, zoom: number, highlighted: boolean): Style {
    let width = 1;
    if (zoom < 9) {
      width = 1;
    } else if (zoom < 12) {
      width = 2;
    } else {
      if (highlighted) {
        width = 8;
      } else {
        width = 4;
      }
    }
    this.defaultRouteStyle.getStroke().setWidth(width);
    this.defaultRouteStyle.getStroke().setColor(color);
    return this.defaultRouteStyle;
  }

  private initRouteStyle() {
    return new Style({
      stroke: new Stroke({
        color: MainStyleColors.green,
        width: 1
      })
    });
  }

}
