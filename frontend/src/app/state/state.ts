import { Injectable } from '@angular/core';
import { SplitState } from '@app/state/split-state';
import { ExploreState } from './explore-state';
import { MapState } from './map-state';
import { PageState } from './page-state';
import { PlannerState } from './planner-state';
import { PreferencesState } from './preferences-state';

@Injectable({
  providedIn: 'root',
})
export class State {
  readonly page = new PageState();
  readonly map = new MapState();
  readonly explore = new ExploreState();
  readonly planner = new PlannerState();
  readonly preferences = new PreferencesState();
  readonly splitState = new SplitState();
}
