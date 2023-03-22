import { selectPreferencesShowProposed } from '@app/core/preferences/preferences.selectors';
import { Store } from '@ngrx/store';
import { StyleFunction } from 'ol/style/Style';
import Style from 'ol/style/Style';
import { MapService } from '../services/map.service';
import { MainMapNodeStyle } from './main-map-node-style';
import { MainMapRouteStyle } from './main-map-route-style';

export class MainMapStyle {
  private readonly invisible = new Style({});
  private readonly mainMapNodeStyle: MainMapNodeStyle;
  private readonly mainMapRouteStyle: MainMapRouteStyle;

  private showProposed: boolean;

  constructor(private mapService: MapService, private store: Store) {
    this.mainMapNodeStyle = new MainMapNodeStyle(mapService);
    this.mainMapRouteStyle = new MainMapRouteStyle(mapService);
    this.store
      .select(selectPreferencesShowProposed)
      .subscribe((showProposed) => {
        this.showProposed = showProposed;
      });
  }

  styleFunction(): StyleFunction {
    return (feature, resolution) => {
      const layer = feature.get('layer');

      let show = true;
      const proposed = feature.get('state') === 'proposed';
      if (proposed && !this.showProposed) {
        show = false;
      }

      if (show) {
        if (layer.includes('node')) {
          return this.mainMapNodeStyle.nodeStyle(resolution, feature);
        }
        return this.mainMapRouteStyle.routeStyle(resolution, feature);
      } else {
        return this.invisible;
      }
    };
  }
}
