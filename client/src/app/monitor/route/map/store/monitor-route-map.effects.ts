import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Params } from '@angular/router';
import { Router } from '@angular/router';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { EditParameters } from '../../../../analysis/components/edit/edit-parameters';
import { selectQueryParams } from '../../../../core/core.state';
import { selectRouteParam } from '../../../../core/core.state';
import { actionSharedEdit } from '../../../../core/shared/shared.actions';
import { MonitorService } from '../../../monitor.service';
import { MonitorRouteMapService } from '../monitor-route-map.service';
import { actionMonitorRouteMapZoomToFitRoute } from './monitor-route-map.actions';
import { actionMonitorRouteMapSelectSubRelation } from './monitor-route-map.actions';
import { actionMonitorRouteMapPageLoad } from './monitor-route-map.actions';
import { actionMonitorRouteMapPositionChanged } from './monitor-route-map.actions';
import { actionMonitorRouteMapMode } from './monitor-route-map.actions';
import { actionMonitorRouteMapMatchesVisible } from './monitor-route-map.actions';
import { actionMonitorRouteMapDeviationsVisible } from './monitor-route-map.actions';
import { actionMonitorRouteMapOsmRelationVisible } from './monitor-route-map.actions';
import { actionMonitorRouteMapReferenceVisible } from './monitor-route-map.actions';
import { actionMonitorRouteMapSelectOsmSegment } from './monitor-route-map.actions';
import { actionMonitorRouteMapJosmZoomToSelectedOsmSegment } from './monitor-route-map.actions';
import { actionMonitorRouteMapJosmZoomToSelectedDeviation } from './monitor-route-map.actions';
import { actionMonitorRouteMapSelectDeviation } from './monitor-route-map.actions';
import { actionMonitorRouteMapJosmLoadRouteRelation } from './monitor-route-map.actions';
import { actionMonitorRouteMapJosmZoomToFitRoute } from './monitor-route-map.actions';
import { actionMonitorRouteMapPageInit } from './monitor-route-map.actions';
import { actionMonitorRouteMapFocus } from './monitor-route-map.actions';
import { actionMonitorRouteMapPageLoaded } from './monitor-route-map.actions';
import { selectMonitorRouteMapState } from './monitor-route-map.selectors';
import { selectMonitorRouteMapSelectedOsmSegment } from './monitor-route-map.selectors';
import { selectMonitorRouteMapSelectedDeviation } from './monitor-route-map.selectors';
import { selectMonitorRouteMapPage } from './monitor-route-map.selectors';
import { selectMonitorRouteMapBounds } from './monitor-route-map.selectors';

@Injectable()
export class MonitorRouteMapEffects {
  // noinspection JSUnusedGlobalSymbols
  mapFocusEffect = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionMonitorRouteMapFocus),
        tap((bounds) => this.mapService.focus(bounds))
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteMapPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('groupName')),
        this.store.select(selectRouteParam('routeName')),
      ]),
      map(([_, groupName, routeName]) =>
        actionMonitorRouteMapPageLoad({ groupName, routeName, relationId: 0 })
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapSelectSubRelation = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteMapSelectSubRelation),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('groupName')),
        this.store.select(selectRouteParam('routeName')),
      ]),
      map(([monitorRouteSubRelation, groupName, routeName]) =>
        actionMonitorRouteMapPageLoad({
          groupName,
          routeName,
          relationId: monitorRouteSubRelation.relationId,
        })
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteMapPageLoad),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('groupName')),
        this.store.select(selectRouteParam('routeName')),
        this.store.select(selectQueryParams),
      ]),
      mergeMap(([{ relationId }, groupName, routeName, queryParams]) =>
        this.monitorService.routeMap(groupName, routeName, relationId).pipe(
          map((response) =>
            actionMonitorRouteMapPageLoaded({
              response,
              queryParams,
            })
          )
        )
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapPageLoaded = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteMapPageLoaded),
      map((response) =>
        actionMonitorRouteMapFocus(response.response.result.bounds)
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapSelectDeviation = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteMapSelectDeviation),
      filter((deviation) => !!deviation),
      map((deviation) => actionMonitorRouteMapFocus(deviation.bounds))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapZoomToFitRoute = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteMapZoomToFitRoute),
      concatLatestFrom(() => this.store.select(selectMonitorRouteMapBounds)),
      map(([_, bounds]) => actionMonitorRouteMapFocus(bounds))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapSelectOsmSegment = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteMapSelectOsmSegment),
      filter((deviation) => !!deviation),
      map((deviation) => actionMonitorRouteMapFocus(deviation.bounds))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapJosmLoadRouteRelation = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteMapJosmLoadRouteRelation),
      concatLatestFrom(() => [this.store.select(selectMonitorRouteMapPage)]),
      map(([_, page]) => {
        const editParameters: EditParameters = {
          relationIds: [page.result.relationId],
          fullRelation: true,
        };
        return actionSharedEdit(editParameters);
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapJosmZoomToFitRoute = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteMapJosmZoomToFitRoute),
      concatLatestFrom(() => [this.store.select(selectMonitorRouteMapBounds)]),
      map(([_, bounds]) => {
        const editParameters: EditParameters = {
          bounds,
        };
        return actionSharedEdit(editParameters);
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapJosmZoomToFitSelectedDeviation = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteMapJosmZoomToSelectedDeviation),
      concatLatestFrom(() => [
        this.store.select(selectMonitorRouteMapSelectedDeviation),
      ]),
      filter(([_, segment]) => !!segment),
      map(([_, segment]) => {
        const editParameters: EditParameters = {
          bounds: segment.bounds,
        };
        return actionSharedEdit(editParameters);
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapJosmZoomToFitSelectedOsmSegment = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteMapJosmZoomToSelectedOsmSegment),
      concatLatestFrom(() => [
        this.store.select(selectMonitorRouteMapSelectedOsmSegment),
      ]),
      filter(([_, segment]) => !!segment),
      map(([_, segment]) => {
        const editParameters: EditParameters = {
          bounds: segment.bounds,
        };
        return actionSharedEdit(editParameters);
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  routeMapQueryParamsEffect = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(
          actionMonitorRouteMapPositionChanged,
          actionMonitorRouteMapReferenceVisible,
          actionMonitorRouteMapMatchesVisible,
          actionMonitorRouteMapDeviationsVisible,
          actionMonitorRouteMapOsmRelationVisible,
          actionMonitorRouteMapMode
        ),
        concatLatestFrom(() => this.store.select(selectMonitorRouteMapState)),
        tap(([_, state]) => {
          let selectedDeviation = 0;
          if (state.selectedDeviation) {
            selectedDeviation = state.selectedDeviation.id;
          }
          let selectedOsmSegment = 0;
          if (state.selectedOsmSegment) {
            selectedOsmSegment = state.selectedOsmSegment.id;
          }
          let queryParams: Params = {
            mode: state.mode,
            reference: state.referenceVisible,
            matches: state.matchesVisible,
            deviations: state.deviationsVisible,
            'osm-relation': state.osmRelationVisible,
            'selected-deviation': selectedDeviation,
            'selected-osm-segment': selectedOsmSegment,
          };
          if (state.mapPosition) {
            queryParams = {
              ...queryParams,
              position: state.mapPosition.toQueryParam(),
            };
          }
          this.router.navigate([], {
            relativeTo: this.activatedRoute,
            queryParams,
            replaceUrl: true, // do not push a new entry to the browser history
            queryParamsHandling: 'merge', // preserve other query params if there are any
          });
        })
      );
    },
    { dispatch: false }
  );

  constructor(
    private actions$: Actions,
    private store: Store,
    private router: Router,
    private monitorService: MonitorService,
    private mapService: MonitorRouteMapService,
    private activatedRoute: ActivatedRoute
  ) {}
}
