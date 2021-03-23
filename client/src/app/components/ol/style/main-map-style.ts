import Map from 'ol/Map';
import {StyleFunction} from 'ol/style/Style';
import {MapService} from '../services/map.service';
import {MainMapNodeStyle} from './main-map-node-style';
import {MainMapRouteStyle} from './main-map-route-style';

export class MainMapStyle {

  private readonly mainMapNodeStyle: MainMapNodeStyle;
  private readonly mainMapRouteStyle: MainMapRouteStyle;

  constructor(private map: Map, private mapService: MapService) {
    this.mainMapNodeStyle = new MainMapNodeStyle(mapService);
    this.mainMapRouteStyle = new MainMapRouteStyle(mapService);
  }

  styleFunction(): StyleFunction {
    return (feature, resolution) => {
      const zoom = this.map.getView().getZoom();
      const layer = feature.get('layer');
      if (layer.includes('node')) {
        return this.mainMapNodeStyle.nodeStyle(zoom, feature);
      }
      return this.mainMapRouteStyle.routeStyle(zoom, feature);
    };
  }
}
