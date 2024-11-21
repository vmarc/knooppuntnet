import { WritableSignal } from '@angular/core';
import { Coordinate } from 'ol/coordinate';
import { FeatureLike } from 'ol/Feature';
import { HooverFeature } from './hoover-feature';
import { HooverState } from './hoover-state';

export type RouteHooverAction = (
  hooverRoutes: Array<HooverFeature>,
  coordinate: Coordinate
) => void;

export class RouteHooverHandler {
  constructor(
    readonly hooverState: WritableSignal<HooverState>,
    readonly clickAction: RouteHooverAction
  ) {}

  handle(features: Array<FeatureLike>, coordinate: Coordinate | null) {
    const hooverRoutes = this.toHooverRoutes(features);
    const newHooverRoutes = this.withoutDuplicates(hooverRoutes);
    if (!this.equalHooverRouteState(this.hooverState(), hooverRoutes, coordinate)) {
      this.hooverState.set(new HooverState(newHooverRoutes, coordinate));
    }
    return true;
  }

  click(coordinate: Coordinate) {
    if (this.hooverState().routes.length > 0) {
      this.clickAction(this.hooverState().routes, coordinate);
      return false; // no need to further propagate to other interactions
    }
    return true;
  }

  private toHooverRoutes(features: Array<FeatureLike>): Array<HooverFeature> {
    const routes = features.flatMap((feature: FeatureLike) => {
      const routeId = feature.get('routeId');
      const name = feature.get('name');
      const scope = feature.get('layer');
      if (name && routeId && scope) {
        return new HooverFeature(routeId, name, scope);
      }
      return null;
    });
    if (routes && routes.length > 0) {
      const message = routes
        .map((route) => `route=${route.routeId}, name=${route.name}, scope=${route.scope}`)
        .join(', ');
      console.log('move event ' + message);
    }
    return routes;
  }

  private withoutDuplicates(hooverRoutes: Array<HooverFeature>): Array<HooverFeature> {
    if (hooverRoutes.length <= 1) {
      return hooverRoutes;
    }
    const result: HooverFeature[] = [];
    result.push(hooverRoutes[0]);
    for (let i = 1; i < hooverRoutes.length; i++) {
      if (!this.arrayContains(result, hooverRoutes[i])) {
        result.push(hooverRoutes[i]);
      }
    }
    return result;
  }

  private arrayContains(result: HooverFeature[], route: HooverFeature): boolean {
    for (let i = 0; i < result.length; i++) {
      if (this.equalHooverRoute(result[i], route)) {
        return true;
      }
    }
    return false;
  }

  private equalHooverRouteState(
    hooverState: HooverState,
    newHooverRoutes: Array<HooverFeature>,
    newCoordinate: Coordinate | null
  ): boolean {
    if (hooverState == null) {
      return false;
    }
    const x = hooverState.coordinate == null ? null : hooverState.coordinate[0];
    const y = hooverState.coordinate == null ? null : hooverState.coordinate[1];
    const newX = newCoordinate == null ? null : newCoordinate[0];
    const newY = newCoordinate == null ? null : newCoordinate[1];

    if (x != newX || y != newY) {
      return false;
    }

    if (hooverState.routes.length !== newHooverRoutes.length) {
      return false;
    } else {
      for (let i = 0; i < hooverState.routes.length; i++) {
        if (!this.equalHooverRoute(hooverState.routes[i], newHooverRoutes[i])) {
          return false;
        }
      }
    }
    return true;
  }

  private equalHooverRoute(a: HooverFeature, b: HooverFeature): boolean {
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
