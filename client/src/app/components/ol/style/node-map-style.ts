import Map from 'ol/Map';
import { StyleFunction } from 'ol/style/Style';
import { green } from './main-style-colors';
import { NodeStyle } from './node-style';
import { nameStyle } from './node-style-builder';
import { RouteStyle } from './route-style';

export class NodeMapStyle {
  private readonly smallNodeStyle = NodeStyle.smallGreen;
  private readonly nameStyle = nameStyle();
  private readonly routeStyle = new RouteStyle();

  constructor(private map: Map) {}

  public styleFunction(): StyleFunction {
    return (feature, resolution) => {
      if (feature) {
        const proposed = feature.get('state') === 'proposed';
        const zoom = this.map.getView().getZoom();
        const layer = feature.get('layer');
        if (layer.includes('node')) {
          if (zoom >= 13) {
            let ref = feature.get('ref');
            const name = feature.get('name');

            if (name && ref === 'o') {
              ref = null;
            }

            const style = proposed
              ? NodeStyle.proposedLargeGreen
              : NodeStyle.largeGreen;

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
          return this.smallNodeStyle;
        }

        return this.routeStyle.style(green, zoom, false, proposed);
      }
    };
  }
}
