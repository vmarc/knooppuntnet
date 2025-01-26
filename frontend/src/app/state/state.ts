import { Injectable } from '@angular/core';
import { ExploreState } from './explore-state';
import { MapState } from './map-state';
import { PageState } from './page-state';
import { PlannerState } from './planner-state';

@Injectable({
  providedIn: 'root',
})
export class State {
  readonly page = new PageState();
  readonly map = new MapState();
  readonly explore = new ExploreState();
  readonly planner = new PlannerState();
}
