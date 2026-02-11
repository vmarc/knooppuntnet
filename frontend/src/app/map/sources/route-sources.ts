import { RouteMapOptions } from '@app/map/sources/route-map-options';
import { RouteSource } from '@app/map/sources/route-source';
import { RouteSourceIds } from '@app/map/sources/route-source-ids';
import { RouteTypes } from '@app/shared/kpn/common/route-types';
import { Map as MaplibreMap } from 'maplibre-gl';

export class RouteSources {
  private canoeRouteSource: RouteSource;
  private cyclingRouteSource: RouteSource;
  private hikingRouteSource: RouteSource;
  private horseRidingRouteSource: RouteSource;
  private inlineSkatingRouteSource: RouteSource;
  private motorboatRouteSource: RouteSource;
  private mtbRouteSource: RouteSource;

  constructor(private map: MaplibreMap) {
    this.canoeRouteSource = this.buildRouteSource('canoe');
    this.cyclingRouteSource = this.buildRouteSource('cycling');
    this.hikingRouteSource = this.buildRouteSource('hiking');
    this.horseRidingRouteSource = this.buildRouteSource('horse-riding');
    this.inlineSkatingRouteSource = this.buildRouteSource('inline-skating');
    this.motorboatRouteSource = this.buildRouteSource('motorboat');
    this.mtbRouteSource = this.buildRouteSource('mtb');
  }

  update(options: RouteMapOptions): void {
    RouteTypes.all.forEach((routeType) => {
      const ids = new RouteSourceIds(routeType);
      ids.layerIds().forEach((layerId) => {
        let visible = false;
        if (options.routeType == routeType) {
          if (options.mapMode === 'standard') {
            if (this.standardLayerVisible(options, ids, layerId)) {
              visible = true;
            }
          } else if (options.mapMode == 'surface') {
            //
          } else if (options.mapMode == 'survey') {
            //
          } else if (options.mapMode == 'analysis') {
            if (this.analysisLayerVisible(options, ids, layerId)) {
              visible = true;
            }
          } else if (options.mapMode == 'route-details') {
            if (layerId == ids.nodeRouteLayerId()) {
              visible = true;
            }
            if (layerId == ids.nodeLayerId()) {
              visible = true;
            }
            if (layerId == ids.nodeNameLayerId()) {
              visible = true;
            }
          } else if (options.mapMode == 'route-segments') {
            if (layerId == ids.nodeRouteSegmentLayerId()) {
              visible = true;
            }
          } else if (options.mapMode == 'route-paths') {
            if (layerId == ids.nodeRoutePathLayerId()) {
              visible = true;
            }
          } else if (options.mapMode == 'monitor') {
            //
          }
        }
        this.map.setLayoutProperty(layerId, 'visibility', visible ? 'visible' : 'none');
      });
    });
  }

  remove(): void {
    this.canoeRouteSource.remove();
    this.cyclingRouteSource.remove();
    this.hikingRouteSource.remove();
    this.horseRidingRouteSource.remove();
    this.inlineSkatingRouteSource.remove();
    this.motorboatRouteSource.remove();
    this.mtbRouteSource.remove();
  }

  private buildRouteSource(routeType: string): RouteSource {
    return new RouteSource(this.map, routeType);
  }

  private standardLayerVisible(
    options: RouteMapOptions,
    ids: RouteSourceIds,
    layerId: string
  ): boolean {
    if (options.scopeInternational && layerId == ids.internationalLayerId()) {
      return true;
    }
    if (options.scopeNational && layerId == ids.nationalLayerId()) {
      return true;
    }
    if (options.scopeRegional && layerId == ids.regionalLayerId()) {
      return true;
    }
    if (options.scopeLocal && layerId == ids.localLayerId()) {
      return true;
    }
    if (options.nodeRoutes && layerId == ids.nodeRouteLayerId()) {
      return true;
    }
    if (options.nodeRoutes && layerId == ids.nodeLayerId()) {
      return true;
    }
    if (options.nodeRoutes && layerId == ids.nodeNameLayerId()) {
      return true;
    }
    return false;
  }

  private analysisLayerVisible(
    options: RouteMapOptions,
    ids: RouteSourceIds,
    layerId: string
  ): boolean {
    if (options.nodeRoutes && layerId == ids.nodeRouteLayerId()) {
      return true;
    }
    if (options.nodeRoutes && layerId == ids.nodeLayerId()) {
      return true;
    }
    if (options.nodeRoutes && layerId == ids.nodeNameLayerId()) {
      return true;
    }
    return false;
  }
}
