import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Params } from '@angular/router';
import { Router } from '@angular/router';
import { MonitorChangesParameters } from '@api/common/monitor/monitor-changes-parameters';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { concatMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { EditParameters } from '../../analysis/components/edit/edit-parameters';
import { selectQueryParams } from '../../core/core.state';
import { AppState } from '../../core/core.state';
import { selectRouteParam } from '../../core/core.state';
import { selectRouteParams } from '../../core/core.state';
import { selectPreferencesPageSize } from '../../core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '../../core/preferences/preferences.selectors';
import { actionSharedEdit } from '../../core/shared/shared.actions';
import { MonitorService } from '../monitor.service';
import { MonitorRouteMapService } from '../route/map/monitor-route-map.service';
import { actionMonitorRouteMapPositionChanged } from './monitor.actions';
import { actionMonitorRouteMapMode } from './monitor.actions';
import { actionMonitorRouteMapMatchesVisible } from './monitor.actions';
import { actionMonitorRouteMapDeviationsVisible } from './monitor.actions';
import { actionMonitorRouteMapOsmRelationVisible } from './monitor.actions';
import { actionMonitorRouteMapReferenceVisible } from './monitor.actions';
import { actionMonitorRouteMapSelectOsmSegment } from './monitor.actions';
import { actionMonitorRouteMapJosmZoomToSelectedOsmSegment } from './monitor.actions';
import { actionMonitorRouteAnalyzed } from './monitor.actions';
import { actionMonitorRouteUploaded } from './monitor.actions';
import { actionMonitorRouteSaved } from './monitor.actions';
import { actionMonitorRouteUploadInit } from './monitor.actions';
import { actionMonitorRouteUpdatePageLoaded } from './monitor.actions';
import { actionMonitorRouteUpdatePageInit } from './monitor.actions';
import { actionMonitorRouteAddPageLoaded } from './monitor.actions';
import { actionMonitorRouteAddPageInit } from './monitor.actions';
import { actionMonitorRouteMapJosmZoomToSelectedDeviation } from './monitor.actions';
import { actionMonitorRouteMapSelectDeviation } from './monitor.actions';
import { actionMonitorRouteMapJosmLoadRouteRelation } from './monitor.actions';
import { actionMonitorRouteMapJosmZoomToFitRoute } from './monitor.actions';
import { actionMonitorRouteDelete } from './monitor.actions';
import { actionMonitorRouteDeletePageInit } from './monitor.actions';
import { actionMonitorRouteSaveInit } from './monitor.actions';
import { actionMonitorRouteInfoLoaded } from './monitor.actions';
import { actionMonitorRouteInfo } from './monitor.actions';
import { actionMonitorChangesPageIndex } from './monitor.actions';
import { actionMonitorRouteChangesPageIndex } from './monitor.actions';
import { actionMonitorGroupChangesPageIndex } from './monitor.actions';
import { actionMonitorChangesPageLoaded } from './monitor.actions';
import { actionMonitorChangesPageInit } from './monitor.actions';
import { actionMonitorGroupChangesPageLoaded } from './monitor.actions';
import { actionMonitorGroupChangesPageInit } from './monitor.actions';
import { actionMonitorGroupPageLoaded } from './monitor.actions';
import { actionMonitorGroupPageInit } from './monitor.actions';
import { actionMonitorGroupDeleteInit } from './monitor.actions';
import { actionMonitorGroupUpdateInit } from './monitor.actions';
import { actionMonitorRouteDetailsPageInit } from './monitor.actions';
import { actionMonitorRouteMapPageInit } from './monitor.actions';
import { actionMonitorRouteChangesPageInit } from './monitor.actions';
import { actionMonitorRouteChangePageInit } from './monitor.actions';
import { actionMonitorGroupsPageInit } from './monitor.actions';
import { actionMonitorGroupUpdateLoaded } from './monitor.actions';
import { actionMonitorGroupUpdate } from './monitor.actions';
import { actionMonitorGroupDelete } from './monitor.actions';
import { actionMonitorGroupDeleteLoaded } from './monitor.actions';
import { actionMonitorGroupAdd } from './monitor.actions';
import { actionMonitorGroupsPageLoaded } from './monitor.actions';
import { actionMonitorRouteChangePageLoaded } from './monitor.actions';
import { actionMonitorRouteMapFocus } from './monitor.actions';
import { actionMonitorRouteMapPageLoaded } from './monitor.actions';
import { actionMonitorRouteDetailsPageLoaded } from './monitor.actions';
import { actionMonitorRouteChangesPageLoaded } from './monitor.actions';
import { selectMonitorState } from './monitor.selectors';
import { selectMonitorRouteMapSelectedOsmSegment } from './monitor.selectors';
import { selectMonitorRouteMapSelectedDeviation } from './monitor.selectors';
import { selectMonitorRouteMapPage } from './monitor.selectors';
import { selectMonitorRouteMapBounds } from './monitor.selectors';
import { selectMonitorGroupName } from './monitor.selectors';
import { selectMonitorChangesPageIndex } from './monitor.selectors';
import { selectMonitorGroupChangesPageIndex } from './monitor.selectors';
import { selectMonitorAdmin } from './monitor.selectors';

@Injectable()
export class MonitorEffects {
  // noinspection JSUnusedGlobalSymbols
  mapFocusEffect = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionMonitorRouteMapFocus),
        tap((bounds) => this.mapService.focus(bounds))
      ),
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  monitorGroupsPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorGroupsPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParams),
        this.store.select(selectMonitorAdmin),
      ]),
      mergeMap(([{}, admin]) => {
        if (admin) {
          return this.monitorService
            .groups()
            .pipe(map((response) => actionMonitorGroupsPageLoaded(response)));
        }
        return this.monitorService
          .groups()
          .pipe(map((response) => actionMonitorGroupsPageLoaded(response)));
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorGroupPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorGroupPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('groupName'))),
      mergeMap(([{}, groupName]) =>
        this.monitorService
          .group(groupName)
          .pipe(map((response) => actionMonitorGroupPageLoaded(response)))
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorGroupChangesPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(
        actionMonitorGroupChangesPageInit,
        actionMonitorGroupChangesPageIndex
      ),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('groupName')),
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectMonitorGroupChangesPageIndex),
        this.store.select(selectPreferencesImpact),
      ]),
      mergeMap(([{}, groupName, pageSize, pageIndex, impact]) => {
        const parameters: MonitorChangesParameters = {
          pageSize,
          pageIndex,
          impact,
        };
        return this.monitorService
          .groupChanges(groupName, parameters)
          .pipe(
            map((response) => actionMonitorGroupChangesPageLoaded(response))
          );
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorGroupDeleteInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorGroupDeleteInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('groupName'))),
      mergeMap(([{}, groupName]) =>
        this.monitorService
          .group(groupName)
          .pipe(map((response) => actionMonitorGroupDeleteLoaded(response)))
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorGroupUpdateInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorGroupUpdateInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('groupName'))),
      mergeMap(([{}, groupName]) =>
        this.monitorService
          .group(groupName)
          .pipe(map((response) => actionMonitorGroupUpdateLoaded(response)))
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteAddPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteAddPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('groupName'))),
      mergeMap(([{}, groupName]) =>
        this.monitorService
          .routeAddPage(groupName)
          .pipe(map((response) => actionMonitorRouteAddPageLoaded(response)))
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteInfo = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteInfo),
      mergeMap((action) =>
        this.monitorService
          .routeInfo(action.relationId)
          .pipe(map((response) => actionMonitorRouteInfoLoaded(response)))
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteAdd = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteSaveInit),
      concatLatestFrom(() => this.store.select(selectMonitorGroupName)),
      mergeMap(([parameters, groupName]) => {
        const properties = parameters.properties;
        if (parameters.mode === 'add') {
          return this.monitorService.routeAdd(groupName, properties).pipe(
            map((response) => {
              if (parameters.gpxFile) {
                return actionMonitorRouteUploadInit(parameters);
              }
              return actionMonitorRouteSaved(response);
            })
          );
        }
        return this.monitorService
          .routeUpdate(groupName, parameters.initialProperties.name, properties)
          .pipe(
            map((response) => {
              if (
                properties.referenceType === 'gpx' &&
                properties.gpxFileChanged
              ) {
                return actionMonitorRouteUploadInit(parameters);
              }
              return actionMonitorRouteSaved(response);
            })
          );
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteGpxUploadInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteUploadInit),
      concatLatestFrom(() => [this.store.select(selectMonitorGroupName)]),
      mergeMap(([parameters, groupName]) => {
        return this.monitorService
          .routeGpxUpload(
            groupName,
            parameters.properties.name,
            parameters.gpxFile
          )
          .pipe(map(() => actionMonitorRouteUploaded(parameters)));
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteGpxUploaded = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteUploaded),
      concatLatestFrom(() => [this.store.select(selectMonitorGroupName)]),
      mergeMap(([parameters, groupName]) => {
        return this.monitorService
          .routeAnalyze(groupName, parameters.properties.name)
          .pipe(map(() => actionMonitorRouteAnalyzed()));
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteUpdatePage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteUpdatePageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('groupName')),
        this.store.select(selectRouteParam('routeName')),
      ]),
      mergeMap(([{}, groupName, routeName]) =>
        this.monitorService
          .routeUpdatePage(groupName, routeName)
          .pipe(map((response) => actionMonitorRouteUpdatePageLoaded(response)))
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteDeleteInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteDeletePageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('groupName')),
        this.store.select(selectRouteParam('routeName')),
      ]),
      mergeMap(([{}, groupName, routeName]) =>
        this.monitorService
          .route(groupName, routeName)
          .pipe(
            map((response) => actionMonitorRouteDetailsPageLoaded(response))
          )
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteDelete = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionMonitorRouteDelete),
        concatLatestFrom(() => [
          this.store.select(selectRouteParam('groupName')),
          this.store.select(selectRouteParam('routeName')),
        ]),
        mergeMap(([{}, groupName, routeName]) =>
          this.monitorService
            .routeDelete(groupName, routeName)
            .pipe(
              tap(() => this.router.navigate([`/monitor/groups/${groupName}`]))
            )
        )
      ),
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteDetailsPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteDetailsPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('groupName')),
        this.store.select(selectRouteParam('routeName')),
      ]),
      mergeMap(([{}, groupName, routeName]) =>
        this.monitorService
          .route(groupName, routeName)
          .pipe(
            map((response) => actionMonitorRouteDetailsPageLoaded(response))
          )
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteMapPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('groupName')),
        this.store.select(selectRouteParam('routeName')),
        this.store.select(selectQueryParams),
      ]),
      mergeMap(([{}, groupName, routeName, queryParams]) =>
        this.monitorService.routeMap(groupName, routeName).pipe(
          map((response) =>
            actionMonitorRouteMapPageLoaded({
              response,
              queryParams,
            })
          )
        )
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapSelectDeviation = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteMapSelectDeviation),
      filter((deviation) => !!deviation),
      map((deviation) => actionMonitorRouteMapFocus(deviation.bounds))
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapSelectOsmSegment = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteMapSelectOsmSegment),
      filter((deviation) => !!deviation),
      map((deviation) => actionMonitorRouteMapFocus(deviation.bounds))
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapJosmLoadRouteRelation = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteMapJosmLoadRouteRelation),
      concatLatestFrom(() => [this.store.select(selectMonitorRouteMapPage)]),
      map(([{}, page]) => {
        const editParameters: EditParameters = {
          relationIds: [page.result.relationId],
          fullRelation: true,
        };
        return actionSharedEdit(editParameters);
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapJosmZoomToFitRoute = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteMapJosmZoomToFitRoute),
      concatLatestFrom(() => [this.store.select(selectMonitorRouteMapBounds)]),
      map(([{}, bounds]) => {
        const editParameters: EditParameters = {
          bounds,
        };
        return actionSharedEdit(editParameters);
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapJosmZoomToFitSelectedDeviation = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteMapJosmZoomToSelectedDeviation),
      concatLatestFrom(() => [
        this.store.select(selectMonitorRouteMapSelectedDeviation),
      ]),
      filter(([{}, segment]) => !!segment),
      map(([{}, segment]) => {
        const editParameters: EditParameters = {
          bounds: segment.bounds,
        };
        return actionSharedEdit(editParameters);
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapJosmZoomToFitSelectedOsmSegment = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteMapJosmZoomToSelectedOsmSegment),
      concatLatestFrom(() => [
        this.store.select(selectMonitorRouteMapSelectedOsmSegment),
      ]),
      filter(([{}, segment]) => !!segment),
      map(([{}, segment]) => {
        const editParameters: EditParameters = {
          bounds: segment.bounds,
        };
        return actionSharedEdit(editParameters);
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteChangesPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(
        actionMonitorRouteChangesPageInit,
        actionMonitorRouteChangesPageIndex
      ),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('monitorRouteId')),
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectMonitorGroupChangesPageIndex),
        this.store.select(selectPreferencesImpact),
      ]),
      mergeMap(([{}, monitorRouteId, pageSize, pageIndex, impact]) => {
        const parameters: MonitorChangesParameters = {
          pageSize,
          pageIndex,
          impact,
        };
        return this.monitorService
          .routeChanges(monitorRouteId, parameters)
          .pipe(
            map((response) => actionMonitorRouteChangesPageLoaded(response))
          );
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteChangePageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteChangePageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('monitorRouteId')),
        this.store.select(selectRouteParam('changeSetId')),
        this.store.select(selectRouteParam('replicationNumber')),
      ]),
      mergeMap(([{}, monitorRouteId, changeSetId, replicationNumber]) =>
        this.monitorService
          .routeChange(monitorRouteId, changeSetId, replicationNumber)
          .pipe(map((response) => actionMonitorRouteChangePageLoaded(response)))
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  addGroupEffect = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionMonitorGroupAdd),
        concatMap((properties) => this.monitorService.groupAdd(properties)),
        tap(() => this.router.navigate(['/monitor']))
      ),
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  deleteGroupEffect = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionMonitorGroupDelete),
        concatMap((action) => this.monitorService.groupDelete(action.groupId)),
        tap(() => this.router.navigate(['/monitor']))
      ),
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  updateGroupEffect = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionMonitorGroupUpdate),
        concatMap((action) =>
          this.monitorService.groupUpdate(action.groupId, action.properties)
        ),
        tap(() => this.router.navigate(['/monitor']))
      ),
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  monitorChangesPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorChangesPageInit, actionMonitorChangesPageIndex),
      concatLatestFrom(() => [
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectMonitorChangesPageIndex),
        this.store.select(selectPreferencesImpact),
      ]),
      mergeMap(([{}, pageSize, pageIndex, impact]) => {
        const parameters: MonitorChangesParameters = {
          pageSize,
          pageIndex,
          impact,
        };
        return this.monitorService
          .changes(parameters)
          .pipe(map((response) => actionMonitorChangesPageLoaded(response)));
      })
    )
  );

  // noinspection JSUnusedGlobalSymbols
  routeMapQueryParamsEffect = createEffect(
    () =>
      this.actions$.pipe(
        ofType(
          actionMonitorRouteMapPositionChanged,
          actionMonitorRouteMapReferenceVisible,
          actionMonitorRouteMapMatchesVisible,
          actionMonitorRouteMapDeviationsVisible,
          actionMonitorRouteMapOsmRelationVisible,
          actionMonitorRouteMapMode
        ),
        concatLatestFrom(() => this.store.select(selectMonitorState)),
        tap(([{}, state]) => {
          let selectedDeviation = 0;
          if (state.routeMapSelectedDeviation) {
            selectedDeviation = state.routeMapSelectedDeviation.id;
          }
          let selectedOsmSegment = 0;
          if (state.routeMapSelectedOsmSegment) {
            selectedOsmSegment = state.routeMapSelectedOsmSegment.id;
          }
          let queryParams: Params = {
            mode: state.mapMode,
            reference: state.mapReferenceVisible,
            matches: state.mapMatchesVisible,
            deviations: state.mapDeviationsVisible,
            'osm-relation': state.mapOsmRelationVisible,
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
      ),
    { dispatch: false }
  );

  constructor(
    private actions$: Actions,
    private store: Store<AppState>,
    private router: Router,
    private monitorService: MonitorService,
    private mapService: MonitorRouteMapService,
    private activatedRoute: ActivatedRoute
  ) {}

  updateQueryParam(name: string, value: string) {
    const queryParams: Params = {};
    queryParams[name] = value;
    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams,
      replaceUrl: true, // do not push a new entry to the browser history
      queryParamsHandling: 'merge', // preserve other query params if there are any
    });
  }
}
