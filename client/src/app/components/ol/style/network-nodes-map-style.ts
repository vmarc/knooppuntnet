import {List} from 'immutable';
import {FeatureLike} from 'ol/Feature';
import Map from 'ol/Map';
import {Style} from 'ol/style';
import {StyleFunction} from 'ol/style/Style';
import {MainStyleColors} from './main-style-colors';
import {NodeStyle} from './node-style';
import {RouteStyle} from './route-style';

export class NetworkNodesMapStyle {

  private readonly smallNodeStyle = NodeStyle.smallGreen;
  private readonly smallNodeStyleGray = NodeStyle.smallGray;

  private readonly largeNodeStyle = NodeStyle.largeGreen;
  private readonly largeNodeStyleGray = NodeStyle.largeGray;

  private readonly routeStyle = new RouteStyle();

  constructor(private map: Map,
              private networkNodeIds: List<number>,
              private networkRouteIds: List<number>) {
  }

  public styleFunction(): StyleFunction {
    return (feature, resolution) => {
      if (feature) {
        const zoom = this.map.getView().getZoom();
        const layer = feature.get('layer');
        if (layer.includes('node')) {
          return this.nodeStyle(feature);
        }
        return this.buildRouteStyle(feature);
      }
    };
  }

  private nodeStyle(feature: FeatureLike): Style {
    const zoom = this.map.getView().getZoom();
    const nodeId = +feature.get('id');
    if (zoom >= 13) {
      const style = this.networkNodeIds.contains(nodeId) ? this.largeNodeStyle : this.largeNodeStyleGray;
      style.getText().setText(feature.get('name'));
      return style;
    }
    return this.networkNodeIds.contains(nodeId) ? this.smallNodeStyle : this.smallNodeStyleGray;
  }

  private buildRouteStyle(feature: FeatureLike): Style {
    const zoom = this.map.getView().getZoom();
    const featureId = feature.get('id');
    const routeId = +featureId.substring(0, featureId.indexOf('-'));
    const routeColor = this.networkRouteIds.contains(routeId) ? MainStyleColors.green : MainStyleColors.gray;
    return this.routeStyle.style(routeColor, zoom, false);
  }
}
