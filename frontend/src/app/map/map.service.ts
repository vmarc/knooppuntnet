import { effect } from '@angular/core';
import { Injectable, inject } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { LngLatBounds } from 'maplibre-gl';
import { Marker } from 'maplibre-gl';
import { FullscreenControl } from 'maplibre-gl';
import { GeolocateControl } from 'maplibre-gl';
import { NavigationControl } from 'maplibre-gl';
import { Map as MaplibreMap } from 'maplibre-gl';
import { RouteSource } from './sources/route-source';
import { FilterSpecification } from '@maplibre/maplibre-gl-style-spec';
import { MapLayerId } from './constants/map-layer-id';
import { State } from '@app/state/state';

@Injectable()
export class MapService {
  private readonly state = inject(State);
  private map: MaplibreMap | null = null;

  constructor() {
    effect(() => {
      const enabled = this.state.map.layers.backgroundLayerEnabled();
      if (this.map) {
        this.updateBackgroundVisibility(enabled ? 'visible' : 'none');
      }
    });
  }

  init(): void {
    const mapLibreMap = new MaplibreMap({
      container: 'map',
      style: '/assets/liberty.json',
      center: [4.46839, 51.46774],
      zoom: 13,
    });
    this.map = mapLibreMap;
    mapLibreMap.showTileBoundaries = true;
    // mapLibreMap.showCollisionBoxes = true;
    this.preventImageMissingWarning(mapLibreMap);

    mapLibreMap.addControl(new FullscreenControl({}));

    mapLibreMap.addControl(
      new NavigationControl({
        showZoom: true,
        showCompass: false,
        visualizePitch: false,
        visualizeRoll: false,
      })
    );

    mapLibreMap.addControl(new GeolocateControl({}));

    // mapLibreMap.on('zoom', () => {
    //   console.log('zoom changed ' + mapLibreMap.getZoom());
    // });

    mapLibreMap.loadImage('/assets/arrow.png').then((response) => {
      mapLibreMap.addImage('node-route-arrow', response.data);
    });

    mapLibreMap.on('load', () => {
      RouteSource.init(mapLibreMap, this.state.preferences.routeType());
    });
  }

  destroy(): void {
    if (this.map) {
      this.map.remove();
    }
  }

  fitBounds(bounds: Bounds): void {
    if (this.map) {
      const b = new LngLatBounds([bounds.minLon, bounds.minLat, bounds.maxLon, bounds.maxLat]);
      this.map.fitBounds(b);
    }
  }

  addMarker(marker: Marker): void {
    if (this.map) {
      marker.addTo(this.map);
    }
  }

  selectRoutes(routeIds: string[]): void {
    const filter: FilterSpecification = ['in', ['get', 'routeId'], ['literal', routeIds]];
    console.log('selectRoutes');
    this.filterRoutes(filter);
  }

  resetRouteSelection(): void {
    this.filterRoutes(null);
  }

  initRouteType(routeType: string): void {
    if (this.map) {
      if (this.map.loaded()) {
        RouteSource.remove(this.map);
        RouteSource.init(this.map, routeType);
      } else {
        this.map.on('load', () => {
          RouteSource.remove(this.map);
          RouteSource.init(this.map, routeType);
        });
      }
    } else {
      console.error('map not initialized while trying to initialize route type');
    }
  }

  hideRouteLayer(): void {
    if (this.map) {
      this.map.setLayoutProperty(MapLayerId.ROUTE, 'visibility', 'none');
    } else {
      console.error('map not initialized while trying to hide route layer');
    }
  }

  showRouteLayer(): void {
    if (this.map) {
      this.map.setLayoutProperty(MapLayerId.ROUTE, 'visibility', 'visible');
    } else {
      console.error('map not initialized while trying to show route layer');
    }
  }

  hideNodeRouteLayer(): void {
    if (this.map) {
      this.map.setLayoutProperty(MapLayerId.NODE_ROUTE, 'visibility', 'none');
      this.map.setLayoutProperty(MapLayerId.NODE_ROUTE_ARROWS, 'visibility', 'none');
      this.map.setLayoutProperty(MapLayerId.NODE, 'visibility', 'none');
      this.map.setLayoutProperty(MapLayerId.NODE_NAME, 'visibility', 'none');
    } else {
      console.error('map not initialized while trying to hide node route layer');
    }
  }

  showNodeRouteLayer(): void {
    if (this.map) {
      this.map.setLayoutProperty(MapLayerId.NODE_ROUTE, 'visibility', 'visible');
      this.map.setLayoutProperty(MapLayerId.NODE_ROUTE_ARROWS, 'visibility', 'visible');
      this.map.setLayoutProperty(MapLayerId.NODE, 'visibility', 'visible');
      this.map.setLayoutProperty(MapLayerId.NODE_NAME, 'visibility', 'visible');
    } else {
      console.error('map not initialized while trying to show node route layer');
    }
  }

  private updateBackgroundVisibility(value: string): void {
    if (this.map) {
      const layers = this.map.getStyle().layers;
      layers.forEach((layer) => {
        if (layer['source'] === 'openmaptiles' || layer.id == 'background') {
          this.map.setLayoutProperty(layer.id, 'visibility', value);
        }
      });
    } else {
      console.error('map not initialized while trying to update background visibility');
    }
  }

  private filterRoutes(filter: FilterSpecification | null): void {
    if (this.map) {
      this.map.setFilter(MapLayerId.NODE_ROUTE, filter);
      // this.map.setFilter(MapLayerId.NODE_ROUTE_ARROWS, filter);
    } else {
      console.error('map not initialized while trying to filter routes');
    }
  }

  private preventImageMissingWarning(map: MaplibreMap): void {
    map.on('styleimagemissing', (e) => {
      // Add a transparent image to prevent the warning
      map.addImage(e.id, {
        width: 1,
        height: 1,
        data: new Uint8Array([0, 0, 0, 0]),
      });
    });
  }
}
