import { Signal } from '@angular/core';
import { StyleFunction } from 'ol/style/Style';
import Style from 'ol/style/Style';
import { MainMapNodeStyle } from './main-map-node-style';
import { MainMapRouteStyle } from './main-map-route-style';
import { MainMapStyleParameters } from './main-map-style-parameters';

export class MainMapStyle {
  private readonly invisible = new Style({});
  private readonly mainMapNodeStyle: MainMapNodeStyle;
  private readonly mainMapRouteStyle: MainMapRouteStyle;

  constructor(private parameters: Signal<MainMapStyleParameters>) {
    this.mainMapNodeStyle = new MainMapNodeStyle();
    this.mainMapRouteStyle = new MainMapRouteStyle();
  }

  styleFunction(): StyleFunction {
    return (feature, resolution) => {
      if (!this.parameters()) {
        return this.invisible;
      }

      const layer = feature.get('layer');
      let show = true;
      const proposed = feature.get('state') === 'proposed';
      if (proposed && !this.parameters().showProposed) {
        show = false;
      }

      if (show) {
        if (layer.includes('node')) {
          return this.mainMapNodeStyle.nodeStyle(this.parameters(), resolution, feature);
        }
        return this.mainMapRouteStyle.routeStyle(this.parameters(), resolution, feature);
      } else {
        return this.invisible;
      }
    };
  }
}
