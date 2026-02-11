import { OpenDataSources } from '@app/map/sources/open-data-sources';
import { RouteMapOptions } from '@app/map/sources/route-map-options';
import { RouteSources } from '@app/map/sources/route-sources';
import { Map as MaplibreMap } from 'maplibre-gl';

export class Sources {
  private route: RouteSources;
  private openData: OpenDataSources;

  constructor(private map: MaplibreMap) {
    this.route = new RouteSources(map);
    this.openData = new OpenDataSources(map);
  }

  remove(): void {
    this.route.remove();
    this.openData.remove();
  }

  updateRouteSources(options: RouteMapOptions): void {
    this.route.update(options);
  }
}
