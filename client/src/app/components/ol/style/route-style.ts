import { Color } from 'ol/color';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import { green } from './main-style-colors';

export class RouteStyle {
  private defaultRouteStyle = this.initRouteStyle();

  style(
    color: Color,
    resolution: number,
    highlighted: boolean,
    proposed: boolean
  ): Style {
    let width = 1;
    if (resolution < /* zoomLevel 9 */ 305.75) {
      width = 1;
    } else if (resolution < /* zoomLevel 12 */ 38.219) {
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
    if (proposed) {
      this.defaultRouteStyle.getStroke().setLineDash([6, 10]);
    } else {
      this.defaultRouteStyle.getStroke().setLineDash(null);
    }
    return this.defaultRouteStyle;
  }

  private initRouteStyle() {
    return new Style({
      stroke: new Stroke({
        color: green,
        width: 1,
      }),
    });
  }
}
