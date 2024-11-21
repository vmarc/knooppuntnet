import { Injectable } from '@angular/core';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';

@Injectable()
export class ExploreStyle {
  private static unselectedRouteStyle = new Style({
    stroke: new Stroke({
      color: [130, 130, 130],
      width: 3,
    }),
  });

  private static readonly selectedRouteStyle = new Style({
    stroke: new Stroke({
      color: [0, 0, 255],
      width: 3,
    }),
  });

  private static readonly internationalRouteStyle = new Style({
    zIndex: 3,
    stroke: new Stroke({
      color: [255, 0, 0],
      width: 8,
    }),
  });
  private static readonly nationalRouteStyle = new Style({
    zIndex: 5,
    stroke: new Stroke({
      color: [0, 0, 255],
      width: 6,
    }),
  });
  private static readonly regionalRouteStyle = new Style({
    zIndex: 10,
    stroke: new Stroke({
      color: [0, 255, 0],
      width: 4,
    }),
  });

  private static localRouteStyle = new Style({
    zIndex: 15,
    stroke: new Stroke({
      color: [255, 125, 0],
      width: 2,
    }),
  });

  static style(scope: string, resolution: number): Style {
    let style: Style = null;
    if (scope == 'international') {
      style = this.internationalRouteStyle;
    } else if (scope == 'national') {
      style = this.nationalRouteStyle;
    } else if (scope == 'regional') {
      style = this.regionalRouteStyle;
    } else {
      style = this.localRouteStyle;
    }

    // if (this.selectedRoute()) {
    //   if (routeId && routeId == this.selectedRoute()) {
    //     style = this.selectedRouteStyle;
    //   } else {
    //     style = this.unselectedRouteStyle;
    //   }
    // } else {
    //   style = this.selectedRouteStyle;
    // }

    return style;
  }
}
