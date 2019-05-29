import {Coordinate} from "ol/coordinate";

import Feature from "ol/Feature";
import Point from "ol/geom/Point";
import Icon from "ol/style/Icon";
import Style from "ol/style/Style";

export class Marker {

  public static create(color: string, coordinate: Coordinate): Feature {
    const style = this.createStyle(color);
    const feature = new Feature(new Point(coordinate));
    feature.setStyle(style);
    return feature;
  }

  private static createStyle(color: string): Style {
    const src = `assets/images/marker-icon-${color}.png`;
    return new Style({
      image: new Icon({
        anchor: [12, 41],
        anchorXUnits: "pixels",
        anchorYUnits: "pixels",
        src: src
      })
    });
  }

}
