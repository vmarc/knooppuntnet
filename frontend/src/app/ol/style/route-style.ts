import { Color } from 'ol/color';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import { green } from './main-style-colors';

export class RouteStyle {
  private defaultRouteStyle = this.initRouteStyle();

  style(color: Color, resolution: number, proposed: boolean): Style {
    let width: number;
    if (resolution > /* zoomLevel 9 */ 305.75) {
      width = 1;
    } else if (resolution > /* zoomLevel 12 */ 38.219) {
      width = 2;
    } else {
      width = 4;
    }

    const stroke = this.defaultRouteStyle.getStroke();
    stroke.setWidth(width);
    stroke.setColor(color);
    if (proposed) {
      stroke.setLineDash([6, 10]);
    } else {
      stroke.setLineDash(null);
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
