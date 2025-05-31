import { OlUtil } from '@app/ol/ol-util';
import { ExploreRoute } from '@app/state/explore-route';
import { MapRoutePopupState } from '@app/state/map-route-popup-state';
import { MapRoutePopupRoute } from '@app/state/map-route-popup-route';
import { State } from '@app/state/state';
import { Coordinate } from 'ol/coordinate';
import { FeatureLike } from 'ol/Feature';

export type MapRoutePopupAction = (
  routes: Array<MapRoutePopupRoute>,
  coordinate: Coordinate
) => void;

export class MapRoutePopupHandler {
  constructor(readonly state: State) {}

  handle(features: Array<FeatureLike>, coordinate: Coordinate | null) {
    if (!this.containsRoutes(features) && this.state.map.routePopupState().routes.length === 0) {
      return true;
    }
    const routes = this.toPopupFeatures(features);
    const newRoutes = this.withoutDuplicates(routes);
    if (!this.equalPopupState(this.state.map.routePopupState(), routes, coordinate)) {
      this.state.map.updateRoutePopupState(new MapRoutePopupState(newRoutes, coordinate));
    }
    return true;
  }

  click(): boolean {
    if (this.state.map.routePopupState().routes.length > 0) {
      const routes = this.state.map
        .routePopupState()
        .routes.map((r) => new ExploreRoute(r.routeId, r.name, r.scope));
      this.state.explore.updateRoutes(routes);
      return false; // no need to further propagate to other interactions
    }
    return true;
  }

  private toPopupFeatures(features: Array<FeatureLike>): Array<MapRoutePopupRoute> {
    if (!this.containsRoutes(features)) {
      return [];
    }

    return features
      .map((feature: FeatureLike) => {
        const featureLayer = OlUtil.featureLayer(feature);
        if (featureLayer == 'node-route' || featureLayer == 'route') {
          const routeId = feature.get('routeId');
          const name = feature.get('name');
          const scope = feature.get('scope');
          if (name && routeId && featureLayer) {
            return new MapRoutePopupRoute(routeId, name, scope);
          }
        }
        return null;
      })
      .filter((x) => x !== null);
  }

  private withoutDuplicates(routes: Array<MapRoutePopupRoute>): Array<MapRoutePopupRoute> {
    if (routes.length <= 1) {
      return routes;
    }
    const routesWithoutDuplicates: MapRoutePopupRoute[] = [];
    routesWithoutDuplicates.push(routes[0]);
    for (let i = 1; i < routes.length; i++) {
      if (!this.arrayContains(routesWithoutDuplicates, routes[i])) {
        routesWithoutDuplicates.push(routes[i]);
      }
    }
    return routesWithoutDuplicates;
  }

  private arrayContains(routes: MapRoutePopupRoute[], route: MapRoutePopupRoute): boolean {
    for (let i = 0; i < routes.length; i++) {
      if (this.equalRoute(routes[i], route)) {
        return true;
      }
    }
    return false;
  }

  private equalPopupState(
    state: MapRoutePopupState,
    newRoutes: Array<MapRoutePopupRoute>,
    newCoordinate: Coordinate | null
  ): boolean {
    if (state == null) {
      return false;
    }
    const x = state.coordinate == null ? null : state.coordinate[0];
    const y = state.coordinate == null ? null : state.coordinate[1];
    const newX = newCoordinate == null ? null : newCoordinate[0];
    const newY = newCoordinate == null ? null : newCoordinate[1];

    if (x != newX || y != newY) {
      return false;
    }

    if (state.routes.length !== newRoutes.length) {
      return false;
    } else {
      for (let i = 0; i < state.routes.length; i++) {
        if (!this.equalRoute(state.routes[i], newRoutes[i])) {
          return false;
        }
      }
    }
    return true;
  }

  private equalRoute(a: MapRoutePopupRoute, b: MapRoutePopupRoute): boolean {
    if (a.routeId === b.routeId) {
      if (a.name === b.name) {
        if (a.scope === b.scope) {
          return true;
        }
      }
    }
    return false;
  }

  private containsRoutes(features: Array<FeatureLike>): boolean {
    for (let i = 0; i < features.length; i++) {
      const feature = features[i];
      const featureLayer = OlUtil.featureLayer(feature);
      if (featureLayer == 'node-route' || featureLayer == 'route') {
        return true;
      }
    }
    return false;
  }
}
