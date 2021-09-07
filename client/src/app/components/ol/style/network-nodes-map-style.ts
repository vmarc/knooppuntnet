import { FeatureLike } from 'ol/Feature';
import Map from 'ol/Map';
import { Style } from 'ol/style';
import { StyleFunction } from 'ol/style/Style';
import { gray } from './main-style-colors';
import { green } from './main-style-colors';
import { NodeStyle } from './node-style';
import { nameStyle } from './node-style-builder';
import { RouteStyle } from './route-style';

export class NetworkNodesMapStyle {
  private readonly smallNodeStyle = NodeStyle.smallGreen;
  private readonly smallNodeStyleGray = NodeStyle.smallGray;
  private readonly nameStyle = nameStyle();

  private readonly routeStyle = new RouteStyle();

  constructor(
    private map: Map,
    private networkNodeIds: number[],
    private networkRouteIds: number[]
  ) {}

  public styleFunction(): StyleFunction {
    return (feature, resolution) => {
      if (feature) {
        const layer = feature.get('layer');
        if (layer.includes('node')) {
          return this.nodeStyle(feature);
        }
        return this.buildRouteStyle(feature);
      }
    };
  }

  private nodeStyle(feature: FeatureLike): Style | Style[] {
    const zoom = this.map.getView().getZoom();
    const nodeId = +feature.get('id');
    if (zoom >= 13) {
      let ref = feature.get('ref');
      const name = feature.get('name');
      const proposed = feature.get('state') === 'proposed';
      if (name && ref === 'o') {
        ref = null;
      }
      let style: Style;
      if (this.networkNodeIds.includes(nodeId)) {
        if (proposed) {
          style = NodeStyle.proposedLargeGreen;
        } else {
          style = NodeStyle.largeGreen;
        }
      } else {
        if (proposed) {
          style = NodeStyle.proposedLargeGray;
        } else {
          style = NodeStyle.largeGray;
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
      ? this.smallNodeStyle
      : this.smallNodeStyleGray;
  }

  private buildRouteStyle(feature: FeatureLike): Style {
    const zoom = this.map.getView().getZoom();
    const featureId = feature.get('id');
    const routeId = +featureId.substring(0, featureId.indexOf('-'));
    const routeColor = this.networkRouteIds.includes(routeId) ? green : gray;
    const proposed = feature.get('state') === 'proposed';
    return this.routeStyle.style(routeColor, zoom, false, proposed);
  }
}
