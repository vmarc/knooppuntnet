import { StyleFunction } from 'ol/style/Style';
import { green } from './main-style-colors';
import { NodeStyle } from './node-style';
import { nameStyle } from './node-style-builder';
import { RouteStyle } from './route-style';

export class NodeMapStyle {
  private readonly smallNodeStyle = NodeStyle.smallGreen;
  private readonly nameStyle = nameStyle();
  private readonly routeStyle = new RouteStyle();

  public styleFunction(): StyleFunction {
    return (feature, resolution) => {
      if (feature) {
        const proposed = feature.get('state') === 'proposed';
        const layer = feature.get('layer');
        if (layer.includes('node')) {
          if (resolution < /* zoomLevel 13 */ 19.109) {
            const ref = feature.get('ref');
            const name = feature.get('name');

            let title: string;
            let subTitle: string;

            if (ref && ref !== 'o') {
              title = ref;
              subTitle = name;
            } else {
              title = name;
            }

            const style = proposed ? NodeStyle.proposedLargeGreen : NodeStyle.largeGreen;

            style.getText().setText(title);

            if (subTitle) {
              this.nameStyle.getText().setText(subTitle);
              return [style, this.nameStyle];
            }
            return style;
          }
          return this.smallNodeStyle;
        }

        return this.routeStyle.style(green, resolution, proposed);
      }
      return null;
    };
  }
}
