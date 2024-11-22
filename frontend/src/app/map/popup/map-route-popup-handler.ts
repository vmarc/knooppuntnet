import { WritableSignal } from '@angular/core';
import { Coordinate } from 'ol/coordinate';
import { FeatureLike } from 'ol/Feature';
import { MapRoutePopupRoute } from './map-route-popup-route';
import { MapRoutePopupState } from './map-route-popup-state';

export type MapRoutePopupAction = (
  routes: Array<MapRoutePopupRoute>,
  coordinate: Coordinate
) => void;

export class MapRoutePopupHandler {
  constructor(
    readonly popupState: WritableSignal<MapRoutePopupState>,
    readonly clickAction: () => void
  ) {}

  handle(features: Array<FeatureLike>, coordinate: Coordinate | null) {
    const routes = this.toPopupFeatures(features);
    const newRoutes = this.withoutDuplicates(routes);
    if (!this.equalPopupState(this.popupState(), routes, coordinate)) {
      this.popupState.set(new MapRoutePopupState(newRoutes, coordinate));
    }
    return true;
  }

  click(): boolean {
    if (this.popupState().routes.length > 0) {
      this.clickAction();
      return false; // no need to further propagate to other interactions
    }
    return true;
  }

  private toPopupFeatures(features: Array<FeatureLike>): Array<MapRoutePopupRoute> {
    return features.flatMap((feature: FeatureLike) => {
      const routeId = feature.get('routeId');
      const name = feature.get('name');
      const scope = feature.get('layer');
      if (name && routeId && scope) {
        return new MapRoutePopupRoute(routeId, name, scope);
      }
      return null;
    });
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
}
