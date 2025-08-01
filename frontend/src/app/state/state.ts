import { Injectable } from '@angular/core';
import { FocusElements } from '@app/state/focus-elements';
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
    this.map.updateSegmentMap(segmentMap);
    this.map.updateMode('route-segments');
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

  routeDetailsPageOpened(routeId: number): void {
    this.map.layers.updateRouteLayerEnabled(true);
    this.map.layers.updateMonitorLayerEnabled(false);
    this.map.updateMode('route-details');
    this.map.updateSelectedRoute(routeId);
  }

  routeMembersPageOpened(routeId: number): void {
    this.map.layers.updateRouteLayerEnabled(true);
    this.map.layers.updateMonitorLayerEnabled(false);
    this.map.updateMode('route-members');
    this.map.updateSelectedRoute(routeId);
  }

  routePathsPageOpened(routeId: number): void {
    this.map.layers.updateRouteLayerEnabled(true);
    this.map.layers.updateMonitorLayerEnabled(false);
    this.map.updateMode('route-paths');
    this.map.updateSelectedRoute(routeId);
  }

  routeSegmentsPageOpened(segmentMap: SegmentMap, routeId: number, relationIds: number[]): void {
    const elements: FocusElements = {
      nodeIds: [],
      routeIds: relationIds.map((id) => id.toString()),
    };
    this.map.updateFocusElements(elements);
    this.map.layers.updateRouteLayerEnabled(true);
    this.map.layers.updateMonitorLayerEnabled(false);
    this.map.updateSegmentMap(segmentMap);
    this.map.updateMode('route-segments');
    this.map.updateSelectedRoute(routeId);
  }
}
