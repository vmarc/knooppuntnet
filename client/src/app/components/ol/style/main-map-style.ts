import { MainMapStyleOptions } from '@app/components/ol/style/main-map-style-options';
import { Subscriptions } from '@app/util/Subscriptions';
import { StyleFunction } from 'ol/style/Style';
import Style from 'ol/style/Style';
import { Observable } from 'rxjs';
import { MainMapNodeStyle } from './main-map-node-style';
import { MainMapRouteStyle } from './main-map-route-style';

export class MainMapStyle {
  private readonly invisible = new Style({});
  private readonly mainMapNodeStyle: MainMapNodeStyle;
  private readonly mainMapRouteStyle: MainMapRouteStyle;

  private options: MainMapStyleOptions;
  private subscriptions = new Subscriptions();

  constructor(private options$: Observable<MainMapStyleOptions>) {
    this.subscriptions.add(
      options$.subscribe((options) => (this.options = options))
    );

    this.mainMapNodeStyle = new MainMapNodeStyle();
    this.mainMapRouteStyle = new MainMapRouteStyle();
  }

  destroy(): void {
    this.subscriptions.unsubscribe();
  }

  styleFunction(): StyleFunction {
    return (feature, resolution) => {
      const layer = feature.get('layer');

      let show = true;
      const proposed = feature.get('state') === 'proposed';
      if (proposed && !this.options.showProposed) {
        show = false;
      }

      if (show) {
        if (layer.includes('node')) {
          return this.mainMapNodeStyle.nodeStyle(
            this.options,
            resolution,
            feature
          );
        }
        return this.mainMapRouteStyle.routeStyle(
          this.options,
          resolution,
          feature
        );
      } else {
        return this.invisible;
      }
    };
  }
}
