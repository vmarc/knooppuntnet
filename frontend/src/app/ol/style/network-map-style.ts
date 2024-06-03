import { FeatureLike } from 'ol/Feature';
import { Style } from 'ol/style';
import { StyleFunction } from 'ol/style/Style';
import { NodeStyle } from './node-style';
import { nameStyle } from './node-style-builder';
import { RouteStyle } from './route-style';
import { StyleColor } from './style-color';

export class NetworkMapStyle {
  private readonly nameStyle = nameStyle();

  private readonly routeStyle = new RouteStyle();

  constructor(
    private networkNodeIds: number[],
    private connectionNodeIds: number[],
    private networkRouteIds: number[],
    private connectionRouteIds: number[]
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
      } else if (this.connectionNodeIds.includes(nodeId)) {
        if (proposed) {
          style = NodeStyle.networkConnectionProposedLarge;
        } else {
          style = NodeStyle.networkConnectionLarge;
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

    let style = NodeStyle.networkOutSmall;
    if (this.networkNodeIds.includes(nodeId)) {
      style = NodeStyle.networkInSmall;
    } else if (this.connectionNodeIds.includes(nodeId)) {
      style = NodeStyle.networkConnectionSmall;
    }
    return style;
  }

  private buildRouteStyle(feature: FeatureLike, resolution: number): Style {
    const featureId = feature.get('id');
    const routeId = +featureId.substring(0, featureId.indexOf('-'));
    const dashed = feature.get('state') === 'proposed';
    let routeColor = StyleColor.networkOut;
    if (this.networkRouteIds.includes(routeId)) {
      routeColor = StyleColor.networkIn;
    } else if (this.connectionRouteIds.includes(routeId)) {
      routeColor = StyleColor.networkConnection;
    }
    return this.routeStyle.style(routeColor, resolution, dashed);
  }
}
