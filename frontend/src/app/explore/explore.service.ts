import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { ExploreRoute } from './explore-route';

@Injectable({
  providedIn: 'root',
})
export class ExploreService {
  private readonly _routes = signal<Array<ExploreRoute>>([]);
  readonly routes = this._routes.asReadonly();

  updateRoutes(routes: Array<ExploreRoute>) {
    this._routes.set(routes);
  }
}
