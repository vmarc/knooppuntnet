import { signal } from '@angular/core';
import { ExploreRoute } from './explore-route';

export class ExploreState {
  private readonly _routes = signal<Array<ExploreRoute>>([]);

  readonly routes = this._routes.asReadonly();

  updateRoutes(routes: Array<ExploreRoute>) {
    this._routes.set(routes);
  }
}
