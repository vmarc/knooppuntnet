import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';
import { ExploreStyleConstants } from './explore-style-constants';

export class RouteSegmentStyle {
  private static readonly segmentStyle = new Style({
    zIndex: ExploreStyleConstants.zIndexStandard,
    stroke: new Stroke({
      color: '#ffff00',
      width: 4,
    }),
  });

  static routeStyle(color: string): Style {
    const style = this.segmentStyle;
    style.getStroke().setColor(color);
    return style;
  }
}
