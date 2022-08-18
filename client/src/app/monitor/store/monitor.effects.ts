import { Injectable } from '@angular/core';
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
import { AppState } from '../../core/core.state';
import { selectRouteParam } from '../../core/core.state';
import { selectRouteParams } from '../../core/core.state';
import { selectPreferencesPageSize } from '../../core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '../../core/preferences/preferences.selectors';
import { actionSharedEdit } from '../../core/shared/shared.actions';
import { MonitorService } from '../monitor.service';
import { MonitorRouteMapService } from '../route/map/monitor-route-map.service';
import { actionMonitorRouteMapJosmZoomToSelectedDeviation } from './monitor.actions';
import { actionMonitorRouteMapSelectDeviation } from './monitor.actions';
import { actionMonitorRouteMapJosmLoadRouteRelation } from './monitor.actions';
import { actionMonitorRouteMapJosmZoomToFitRoute } from './monitor.actions';
import { actionMonitorRouteDelete } from './monitor.actions';
import { actionMonitorRouteDeletePageInit } from './monitor.actions';
import { actionMonitorRouteAdd } from './monitor.actions';
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
        tap((action) => this.mapService.focus(action.bounds))
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
            .pipe(
              map((response) => actionMonitorGroupsPageLoaded({ response }))
            );
        }
        return this.monitorService
          .groups()
          .pipe(map((response) => actionMonitorGroupsPageLoaded({ response })));
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
          .pipe(map((response) => actionMonitorGroupPageLoaded({ response })))
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
            map((response) => actionMonitorGroupChangesPageLoaded({ response }))
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
          .pipe(map((response) => actionMonitorGroupDeleteLoaded({ response })))
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
          .pipe(map((response) => actionMonitorGroupUpdateLoaded({ response })))
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteInfo = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteInfo),
      mergeMap((action) =>
        this.monitorService
          .routeInfo(action.routeId)
          .pipe(map((response) => actionMonitorRouteInfoLoaded({ response })))
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteAdd = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionMonitorRouteAdd),
        concatLatestFrom(() => this.store.select(selectMonitorGroupName)),
        mergeMap(([action, groupName]) =>
          this.monitorService.addRoute(groupName, action.add).pipe(
            tap(() => {
              this.router.navigate([
                `/monitor/groups/${groupName}/routes/${action.add.name}/reference`,
              ]);
            })
          )
        )
      ),
    { dispatch: false }
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
            map((response) => actionMonitorRouteDetailsPageLoaded({ response }))
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
            .deleteRoute(groupName, routeName)
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
            map((response) => actionMonitorRouteDetailsPageLoaded({ response }))
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
      ]),
      mergeMap(([{}, groupName, routeName]) =>
        this.monitorService
          .routeMap(groupName, routeName)
          .pipe(
            map((response) => actionMonitorRouteMapPageLoaded({ response }))
          )
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteMapSelectNokSegment = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteMapSelectDeviation),
      filter((action) => !!action.deviation),
      map((action) =>
        actionMonitorRouteMapFocus({ bounds: action.deviation.bounds })
      )
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
        return actionSharedEdit({ editParameters });
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
        return actionSharedEdit({ editParameters });
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
        return actionSharedEdit({ editParameters });
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
            map((response) => actionMonitorRouteChangesPageLoaded({ response }))
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
          .pipe(
            map((response) => actionMonitorRouteChangePageLoaded({ response }))
          )
      )
    )
  );

  // noinspection JSUnusedGlobalSymbols
  addGroupEffect = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionMonitorGroupAdd),
        concatMap((action) => this.monitorService.addGroup(action.properties)),
        tap(() => this.router.navigate(['/monitor']))
      ),
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  deleteGroupEffect = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionMonitorGroupDelete),
        concatMap((action) => this.monitorService.deleteGroup(action.groupId)),
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
          this.monitorService.updateGroup(action.groupId, action.properties)
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
          .pipe(
            map((response) => actionMonitorChangesPageLoaded({ response }))
          );
      })
    )
  );

  constructor(
    private actions$: Actions,
    private store: Store<AppState>,
    private router: Router,
    private monitorService: MonitorService,
    private mapService: MonitorRouteMapService
  ) {}
}
