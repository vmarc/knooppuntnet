import Circle from 'ol/style/Circle';
import Fill from 'ol/style/Fill';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import { ExploreStyleConstants } from './explore-style-constants';

export class ExploreStyleFocus {
  private static readonly focusColor = '#ff0';

  static readonly smallNode = new Style({
    zIndex: ExploreStyleConstants.zIndexFocus,
    image: new Circle({
      radius: 8,
      fill: new Fill({
        color: this.focusColor,
      }),
    }),
  });

  static readonly largeNode = new Style({
    zIndex: ExploreStyleConstants.zIndexFocus,
    image: new Circle({
      radius: 20,
      fill: new Fill({
        color: this.focusColor,
      }),
    }),
  });

  private static readonly largeRouteStyle = new Style({
    zIndex: ExploreStyleConstants.zIndexFocus,
    stroke: new Stroke({
      color: this.focusColor,
      width: 16,
    }),
  });

  static nodeStyle(zoom: number): Style | undefined {
    let style: Style | undefined = undefined;
    if (zoom >= 13) {
      style = this.largeNode;
    } else if (zoom >= 10) {
      style = this.smallNode;
    }
    return style;
  }

  static routeStyle(zoom: number): Style {
    return this.largeRouteStyle;
  }
}
