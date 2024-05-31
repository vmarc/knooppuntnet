import { FeatureLike } from 'ol/Feature';
import { Style } from 'ol/style';
import { StyleFunction } from 'ol/style/Style';
import { NodeStyle } from './node-style';
import { nameStyle } from './node-style-builder';
import { RouteStyle } from './route-style';
import { StyleColor } from './style-color';

export class NetworkNodesMapStyle {
  private readonly nameStyle = nameStyle();

  private readonly routeStyle = new RouteStyle();

  constructor(
    private networkNodeIds: number[],
    private networkRouteIds: number[]
  ) {}

  public styleFunction(): StyleFunction {
    return (feature, resolution) => {
      if (feature) {
        const layer = feature.get('layer');
        if (layer.includes('node')) {
          return this.nodeStyle(feature, resolution);
        }
        return this.buildRouteStyle(feature, resolution);
      }
      return null;
    };
  }

  private nodeStyle(feature: FeatureLike, resolution: number): Style | Style[] {
    const nodeId = +feature.get('id');
    if (resolution < /* zoomLevel 13 */ 19.109) {
      let ref = feature.get('ref');
      const name = feature.get('name');
      const proposed = feature.get('state') === 'proposed';
      if (name && ref === 'o') {
        ref = null;
      }
      let style: Style;
      if (this.networkNodeIds.includes(nodeId)) {
        if (proposed) {
          style = NodeStyle.networkInProposedLarge;
        } else {
          style = NodeStyle.networkInLarge;
        }
      } else {
        if (proposed) {
          style = NodeStyle.networkOutProposedLarge;
        } else {
          style = NodeStyle.networkOutLarge;
        }
      }
      style.getText().setText(ref);
      if (name) {
        let offsetY = 0;
        if (ref) {
          offsetY = 18;
        }
        this.nameStyle.getText().setText(name);
        this.nameStyle.getText().setOffsetY(offsetY);
        return [style, this.nameStyle];
      }
      return style;
    }
    return this.networkNodeIds.includes(nodeId)
      ? NodeStyle.networkInSmall
      : NodeStyle.networkOutSmall;
  }

  private buildRouteStyle(feature: FeatureLike, resolution: number): Style {
    const featureId = feature.get('id');
    const routeId = +featureId.substring(0, featureId.indexOf('-'));
    const routeColor = this.networkRouteIds.includes(routeId)
      ? StyleColor.networkIn
      : StyleColor.networkOut;
    const proposed = feature.get('state') === 'proposed';
    return this.routeStyle.style(routeColor, resolution, proposed);
  }
}
