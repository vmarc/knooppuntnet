import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Params } from '@angular/router';
import { Router } from '@angular/router';
import { EditParameters } from '@app/analysis/components/edit';
import { MapPosition } from '@app/components/ol/domain';
import { Util } from '@app/components/shared';
import { selectQueryParam } from '@app/core';
import { selectQueryParams } from '@app/core';
import { selectRouteParam } from '@app/core';
import { actionSharedEdit } from '@app/core';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { of } from 'rxjs';
import { filter } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { MonitorService } from '../../../monitor.service';
import { MonitorRouteMapService } from '../monitor-route-map.service';
import { actionMonitorRouteMapPageViewInit } from './monitor-route-map.actions';
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
        tap((bounds) => this.monitorRouteMapService.focus(bounds))
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
        this.store.select(selectQueryParam('sub-relation-id')),
      ]),
      map(([_, groupName, routeName, subRelationIdParameter]) => {
        let relationId = 0;
        if (subRelationIdParameter) {
          relationId = Util.toInteger(subRelationIdParameter);
        }
        return actionMonitorRouteMapPageLoad({
          groupName,
          routeName,
          relationId,
        });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapPageViewInit = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionMonitorRouteMapPageViewInit),
        concatLatestFrom(() => [
          this.store.select(selectMonitorRouteMapPage),
          this.store.select(selectQueryParam('position')),
        ]),
        tap(([_, page, mapPosition]) => {
          const mapPositionFromUrl = MapPosition.fromQueryParam(mapPosition);
          this.monitorRouteMapService.init(page, mapPositionFromUrl);
        })
      );
    },
    {
      dispatch: false,
    }
  );

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
        this.store.select(selectMonitorRouteMapState),
      ]),
      mergeMap(([{ relationId }, groupName, routeName, queryParams, state]) => {
        const page = state.pages?.get(relationId);
        if (page) {
          return of(
            actionMonitorRouteMapPageLoaded({
              page,
              queryParams,
            })
          );
        } else {
          return this.monitorService
            .routeMap(groupName, routeName, relationId)
            .pipe(
              map((response) =>
                // TODO handle case where response.result is absent
                actionMonitorRouteMapPageLoaded({
                  page: response.result,
                  queryParams,
                })
              )
            );
        }
      })
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
          relationIds: [page.relationId],
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
          const subRelationId = state.page?.currentSubRelation?.relationId ?? 0;

          let queryParams: Params = {
            mode: state.mode,
            reference: state.referenceVisible,
            matches: state.matchesVisible,
            deviations: state.deviationsVisible,
            'osm-relation': state.osmRelationVisible,
            'selected-deviation': selectedDeviation,
            'selected-osm-segment': selectedOsmSegment,
            'sub-relation-id': subRelationId,
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
    private monitorRouteMapService: MonitorRouteMapService,
    private activatedRoute: ActivatedRoute
  ) {}
}
