import { large } from '@app/ol/style';
import { small } from '@app/ol/style';
import Stroke from 'ol/style/Stroke';
import Style from 'ol/style/Style';

export class ExploreStyleUnfocused {
  static readonly smallNode = small('#aaa');
  static readonly largeNode = large('#aaa');

  private static readonly largeRouteStyle = new Style({
    zIndex: 11,
    stroke: new Stroke({
      color: '#aaa',
      width: 4,
    }),
  });

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

  static routeStyle(zoom: number): Style {
    return this.largeRouteStyle;
  }
}
