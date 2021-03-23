import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {MonitorChangesParameters} from '@api/common/monitor/monitor-changes-parameters';
import {Actions} from '@ngrx/effects';
import {createEffect} from '@ngrx/effects';
import {ofType} from '@ngrx/effects';
import {Store} from '@ngrx/store';
import {concatMap} from 'rxjs/operators';
import {tap} from 'rxjs/operators';
import {withLatestFrom} from 'rxjs/operators';
import {map} from 'rxjs/operators';
import {mergeMap} from 'rxjs/operators';
import {AppState} from '../../core/core.state';
import {selectRouteParam} from '../../core/core.state';
import {selectRouteParams} from '../../core/core.state';
import {selectPreferencesItemsPerPage} from '../../core/preferences/preferences.selectors';
import {selectPreferencesImpact} from '../../core/preferences/preferences.selectors';
import {LongdistanceRouteMapService} from '../longdistance/route/map/longdistance-route-map.service';
import {MonitorService} from '../monitor.service';
import {MonitorRouteMapService} from '../route/map/monitor-route-map.service';
import {actionMonitorChangesPageIndex} from './monitor.actions';
import {actionMonitorRouteChangesPageIndex} from './monitor.actions';
import {actionMonitorGroupChangesPageIndex} from './monitor.actions';
import {actionMonitorChangesPageLoaded} from './monitor.actions';
import {actionMonitorChangesPageInit} from './monitor.actions';
import {actionMonitorGroupChangesPageLoaded} from './monitor.actions';
import {actionMonitorGroupChangesPageInit} from './monitor.actions';
import {actionMonitorGroupPageLoaded} from './monitor.actions';
import {actionMonitorGroupPageInit} from './monitor.actions';
import {actionLongdistanceRouteMapFocus} from './monitor.actions';
import {actionLongdistanceRouteChangeLoaded} from './monitor.actions';
import {actionLongdistanceRouteChangesLoaded} from './monitor.actions';
import {actionLongdistanceRouteMapLoaded} from './monitor.actions';
import {actionLongdistanceRouteDetailsLoaded} from './monitor.actions';
import {actionLongdistanceRoutesLoaded} from './monitor.actions';
import {actionLongdistanceRouteChangeInit} from './monitor.actions';
import {actionLongdistanceRouteChangesInit} from './monitor.actions';
import {actionLongdistanceRouteMapInit} from './monitor.actions';
import {actionLongdistanceRouteDetailsInit} from './monitor.actions';
import {actionLongdistanceRoutesInit} from './monitor.actions';
import {actionMonitorGroupDeleteInit} from './monitor.actions';
import {actionMonitorGroupUpdateInit} from './monitor.actions';
import {actionMonitorRouteDetailsPageInit} from './monitor.actions';
import {actionMonitorRouteMapPageInit} from './monitor.actions';
import {actionMonitorRouteChangesPageInit} from './monitor.actions';
import {actionMonitorRouteChangePageInit} from './monitor.actions';
import {actionMonitorGroupsPageInit} from './monitor.actions';
import {actionMonitorGroupUpdateLoaded} from './monitor.actions';
import {actionMonitorGroupUpdate} from './monitor.actions';
import {actionMonitorGroupDelete} from './monitor.actions';
import {actionMonitorGroupDeleteLoaded} from './monitor.actions';
import {actionMonitorGroupAdd} from './monitor.actions';
import {actionMonitorGroupsPageLoaded} from './monitor.actions';
import {actionMonitorRouteChangePageLoaded} from './monitor.actions';
import {actionMonitorRouteMapFocus} from './monitor.actions';
import {actionMonitorRouteMapPageLoaded} from './monitor.actions';
import {actionMonitorRouteDetailsPageLoaded} from './monitor.actions';
import {actionMonitorRouteChangesPageLoaded} from './monitor.actions';
import {selectMonitorChangesPageIndex} from './monitor.selectors';
import {selectMonitorGroupChangesPageIndex} from './monitor.selectors';
import {selectMonitorAdmin} from './monitor.selectors';

@Injectable()
export class MonitorEffects {

  mapFocusEffect = createEffect(() =>
      this.actions$.pipe(
        ofType(actionMonitorRouteMapFocus),
        tap(action => this.mapService.focus(action.bounds))
      ),
    {dispatch: false}
  );

  monitorGroupsPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorGroupsPageInit),
      withLatestFrom(
        this.store.select(selectRouteParams),
        this.store.select(selectMonitorAdmin)
      ),
      mergeMap(([params, admin]) => {
        if (admin) {
          return this.monitorService.monitorAdminGroups().pipe(
            map(response => actionMonitorGroupsPageLoaded({response}))
          );
        }
        return this.monitorService.monitorGroups().pipe(
          map(response => actionMonitorGroupsPageLoaded({response}))
        );
      })
    )
  );

  monitorGroupPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorGroupPageInit),
      withLatestFrom(
        this.store.select(selectRouteParam('groupName'))
      ),
      mergeMap(([action, groupName]) => this.monitorService.monitorGroup(groupName).pipe(
          map(response => actionMonitorGroupPageLoaded({response}))
        ))
    )
  );

  monitorGroupChangesPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorGroupChangesPageInit, actionMonitorGroupChangesPageIndex),
      withLatestFrom(
        this.store.select(selectRouteParam('groupName')),
        this.store.select(selectPreferencesItemsPerPage),
        this.store.select(selectMonitorGroupChangesPageIndex),
        this.store.select(selectPreferencesImpact)
      ),
      mergeMap(([action, groupName, itemsPerPage, pageIndex, impact]) => {
        const parameters: MonitorChangesParameters = {
          itemsPerPage,
          pageIndex,
          impact
        };
        return this.monitorService.monitorGroupChanges(groupName, parameters).pipe(
          map(response => actionMonitorGroupChangesPageLoaded({response}))
        );
      })
    )
  );

  monitorGroupDeleteInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorGroupDeleteInit),
      withLatestFrom(
        this.store.select(selectRouteParam('groupName'))
      ),
      mergeMap(([action, groupName]) => this.monitorService.monitorAdminRouteGroup(groupName).pipe(
          map(response => actionMonitorGroupDeleteLoaded({response}))
        ))
    )
  );

  monitorGroupUpdateInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorGroupUpdateInit),
      withLatestFrom(
        this.store.select(selectRouteParam('groupName'))
      ),
      mergeMap(([action, groupName]) => this.monitorService.monitorAdminRouteGroup(groupName).pipe(
          map(response => actionMonitorGroupUpdateLoaded({response}))
        ))
    )
  );

  monitorRouteDetailsPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteDetailsPageInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId'))
      ),
      mergeMap(([action, routeId]) => this.monitorService.monitorRoute(routeId).pipe(
          map(response => actionMonitorRouteDetailsPageLoaded({response}))
        ))
    )
  );

  monitorRouteMapPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteMapPageInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId'))
      ),
      mergeMap(([action, routeId]) => this.monitorService.monitorRouteMap(routeId).pipe(
          map(response => actionMonitorRouteMapPageLoaded({response}))
        ))
    )
  );

  monitorRouteChangesPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteChangesPageInit, actionMonitorRouteChangesPageIndex),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId')),
        this.store.select(selectPreferencesItemsPerPage),
        this.store.select(selectMonitorGroupChangesPageIndex),
        this.store.select(selectPreferencesImpact)
      ),
      mergeMap(([action, routeId, itemsPerPage, pageIndex, impact]) => {
        const parameters: MonitorChangesParameters = {
          itemsPerPage,
          pageIndex,
          impact
        };
        return this.monitorService.monitorRouteChanges(routeId, parameters).pipe(
          map(response => actionMonitorRouteChangesPageLoaded({response}))
        );
      })
    )
  );

  monitorRouteChangePageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteChangePageInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId')),
        this.store.select(selectRouteParam('changeSetId')),
        this.store.select(selectRouteParam('replicationNumber'))
      ),
      mergeMap(([action, routeId, changeSetId, replicationNumber]) => this.monitorService.monitorRouteChange(routeId, changeSetId, replicationNumber).pipe(
          map(response => actionMonitorRouteChangePageLoaded({response}))
        ))
    )
  );

  addGroupEffect$ = createEffect(() =>
      this.actions$.pipe(
        ofType(actionMonitorGroupAdd),
        concatMap((action) => this.monitorService.monitorAdminAddRouteGroup(action.group)),
        tap(() => this.router.navigate(['/monitor']))
      ),
    {dispatch: false}
  );

  deleteGroupEffect$ = createEffect(() =>
      this.actions$.pipe(
        ofType(actionMonitorGroupDelete),
        concatMap((action) => this.monitorService.monitorAdminDeleteRouteGroup(action.groupName)),
        tap(() => this.router.navigate(['/monitor']))
      ),
    {dispatch: false}
  );

  updateGroupEffect$ = createEffect(() =>
      this.actions$.pipe(
        ofType(actionMonitorGroupUpdate),
        concatMap((action) => this.monitorService.monitorAdminUpdateRouteGroup(action.group)),
        tap(() => this.router.navigate(['/monitor']))
      ),
    {dispatch: false}
  );

  monitorChangesPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorChangesPageInit, actionMonitorChangesPageIndex),
      withLatestFrom(
        this.store.select(selectPreferencesItemsPerPage),
        this.store.select(selectMonitorChangesPageIndex),
        this.store.select(selectPreferencesImpact)
      ),
      mergeMap(([action, itemsPerPage, pageIndex, impact]) => {
        const parameters: MonitorChangesParameters = {
          itemsPerPage,
          pageIndex,
          impact
        };
        return this.monitorService.monitorChanges(parameters).pipe(
          map(response => actionMonitorChangesPageLoaded({response}))
        );
      })
    )
  );

  /********************************************************/

  longdistanceRouteMapFocusEffect = createEffect(() =>
      this.actions$.pipe(
        ofType(actionLongdistanceRouteMapFocus),
        tap(action => this.longdistanceRouteMapService.focus(action.bounds))
      ),
    {dispatch: false}
  );

  longdistanceRoutesInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionLongdistanceRoutesInit),
      mergeMap((action) => this.monitorService.longdistanceRoutes().pipe(
          map(response => actionLongdistanceRoutesLoaded({response}))
        ))
    )
  );

  longdistanceRouteDetailsInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionLongdistanceRouteDetailsInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId'))
      ),
      mergeMap(([action, routeId]) => this.monitorService.longdistanceRoute(routeId).pipe(
          map(response => actionLongdistanceRouteDetailsLoaded({response}))
        ))
    )
  );

  longdistanceRouteMapInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionLongdistanceRouteMapInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId'))
      ),
      mergeMap(([action, routeId]) => this.monitorService.longdistanceRouteMap(routeId).pipe(
          map(response => actionLongdistanceRouteMapLoaded({response}))
        ))
    )
  );

  longdistanceRouteChangesInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionLongdistanceRouteChangesInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId'))
      ),
      mergeMap(([action, routeId]) => this.monitorService.longdistanceRouteChanges(routeId).pipe(
          map(response => actionLongdistanceRouteChangesLoaded({response}))
        ))
    )
  );

  longdistanceRouteChangeInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionLongdistanceRouteChangeInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId')),
        this.store.select(selectRouteParam('changeSetId'))
      ),
      mergeMap(([action, routeId, changeSetId]) => this.monitorService.longdistanceRouteChange(routeId, changeSetId).pipe(
          map(response => actionLongdistanceRouteChangeLoaded({response}))
        ))
    )
  );

  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private router: Router,
              private monitorService: MonitorService,
              private mapService: MonitorRouteMapService,
              private longdistanceRouteMapService: LongdistanceRouteMapService) {
  }

}
