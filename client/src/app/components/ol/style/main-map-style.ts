import { Store } from '@ngrx/store';
import Map from 'ol/Map';
import { StyleFunction } from 'ol/style/Style';
import { AppState } from '../../../core/core.state';
import { selectPreferencesShowProposed } from '../../../core/preferences/preferences.selectors';
import { MapService } from '../services/map.service';
import { MainMapNodeStyle } from './main-map-node-style';
import { MainMapRouteStyle } from './main-map-route-style';
import Style from 'ol/style/Style';

export class MainMapStyle {
  private readonly invisible = new Style({});
  private readonly mainMapNodeStyle: MainMapNodeStyle;
  private readonly mainMapRouteStyle: MainMapRouteStyle;

  private showProposed: boolean;

  constructor(
    private map: Map,
    private mapService: MapService,
    private store: Store<AppState>
  ) {
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
      const zoom = this.map.getView().getZoom();
      const layer = feature.get('layer');

      let show = true;
      const proposed = feature.get('state') === 'proposed';
      if (proposed && !this.showProposed) {
        show = false;
      }

      if (show) {
        if (layer.includes('node')) {
          return this.mainMapNodeStyle.nodeStyle(zoom, feature);
        }
        return this.mainMapRouteStyle.routeStyle(zoom, feature);
      } else {
        return this.invisible;
      }
    };
  }
}
