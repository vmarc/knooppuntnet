import { OlUtil } from '@app/ol/ol-util';
import { FeatureLike } from 'ol/Feature';
import Style from 'ol/style/Style';
import { StyleFunction } from 'ol/style/Style';
import { NodeStyle } from './node-style';
import { nameStyle } from './node-style-builder';
import { RouteStyle } from './route-style';
import { StyleColor } from './style-color';

export class NodeMapStyle {
  private readonly nameStyle = nameStyle();
  private readonly routeStyle = new RouteStyle();

  public styleFunction(): StyleFunction {
    return (feature, resolution) => {
      if (feature) {
        const proposed = feature.get('state') === 'proposed';
        const featureLayer = OlUtil.featureLayer(feature);
        if (featureLayer.includes('node')) {
          return this.nodeStyle(feature, resolution, proposed);
        }
        const color = StyleColor.routeColorAnalysis(feature);
        return this.routeStyle.style(color, resolution, proposed);
      }
      return null;
    };
  }

  private nodeStyle(feature: FeatureLike, resolution: number, proposed: boolean): Style | Style[] {
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

      const style = proposed ? NodeStyle.defaultProposedLarge : NodeStyle.defaultLarge;

      style.getText().setText(title);

      if (subTitle) {
        this.nameStyle.getText().setText(subTitle);
        return [style, this.nameStyle];
      }
      return style;
    }
    return NodeStyle.defaultSmall;
  }
}
