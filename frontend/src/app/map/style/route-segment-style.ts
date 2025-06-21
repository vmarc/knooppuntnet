import { SegmentColors } from '@app/map/domain/segment-colors';
import { small } from '@app/ol/style/node-style-builder';
import { large } from '@app/ol/style/node-style-builder';
import { MapStyleOptions } from '@app/state/map-style-options';
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

  private static readonly otherSegmentStyle = new Style({
    zIndex: ExploreStyleConstants.zIndexStandard,
    stroke: new Stroke({
      color: '#aaaaaa',
      width: 2,
    }),
  });

  static readonly smallNode = small('#aaaaaa');
  static readonly largeNode = large('#aaaaaa');

  static nodeStyle(zoom: number, title: string): Style {
    let style: Style | undefined = undefined;
    if (zoom >= 13) {
      style = this.largeNode;
      style.getText().setText(title);
    } else if (zoom >= 10) {
      style = this.smallNode;
    }
    return style;
  }

  static routeStyle(
    styleOptions: MapStyleOptions,
    routeId: number,
    segmentId: number,
    segmentElementId: number
  ): Style {
    if (styleOptions.selectedRoute === routeId) {
      const style = this.segmentStyle;
      const color = SegmentColors.colorForSegmentId(segmentId);
      style.getStroke().setColor(color);
      return style;
    }
    return undefined;
  }
}
