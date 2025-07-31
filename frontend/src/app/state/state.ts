import { Injectable } from '@angular/core';
import { SegmentMap } from '@app/state/segment-map';
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

  monitorSegmentsPageOpened(routeId: string, relationIds: number[], segmentMap: SegmentMap): void {
    this.map.layers.updateRouteLayerEnabled(false);
    this.map.layers.updateMonitorLayerEnabled(true);
    this.map.updateMode('route-segments');
    this.map.updateSegmentMap(segmentMap);
    this.map.updateMonitorMode('segments');
    this.map.updateMonitorRouteIds([routeId]);
    this.map.updateMonitorRelationIds(relationIds);
  }

  monitorPageOpened(routeId: string, relationIds: number[]): void {
    this.map.layers.updateRouteLayerEnabled(false);
    this.map.layers.updateMonitorLayerEnabled(true);
    this.map.updateMode('monitor');
    this.map.updateMonitorMode('route');
    this.map.updateSubject('explore');
    this.map.updateMonitorRouteIds([routeId]);
    this.map.updateMonitorRelationIds(relationIds);
  }

  routePageOpened(routeId: number): void {
    this.map.layers.updateRouteLayerEnabled(true);
    this.map.layers.updateMonitorLayerEnabled(false);
    this.map.updateSelectedRoute(routeId);
  }
}
