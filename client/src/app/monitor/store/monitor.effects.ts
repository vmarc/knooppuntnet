import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Actions} from '@ngrx/effects';
import {createEffect} from '@ngrx/effects';
import {ofType} from '@ngrx/effects';
import {Store} from '@ngrx/store';
import {concatMap} from 'rxjs/operators';
import {tap} from 'rxjs/operators';
import {withLatestFrom} from 'rxjs/operators';
import {map} from 'rxjs/operators';
import {mergeMap} from 'rxjs/operators';
import {AppService} from '../../app.service';
import {AppState} from '../../core/core.state';
import {selectRouteParam} from '../../core/core.state';
import {selectRouteParams} from '../../core/core.state';
import {LongdistanceRouteMapService} from '../longdistance/route/map/longdistance-route-map.service';
import {MonitorRouteMapService} from '../route/map/monitor-route-map.service';
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
import {actionMonitorRouteDetailsInit} from './monitor.actions';
import {actionMonitorRouteMapInit} from './monitor.actions';
import {actionMonitorRouteChangesInit} from './monitor.actions';
import {actionMonitorRouteChangeInit} from './monitor.actions';
import {actionMonitorGroupsPageInit} from './monitor.actions';
import {actionMonitorGroupUpdateLoaded} from './monitor.actions';
import {actionMonitorGroupUpdate} from './monitor.actions';
import {actionMonitorGroupDelete} from './monitor.actions';
import {actionMonitorGroupDeleteLoaded} from './monitor.actions';
import {actionMonitorGroupAdd} from './monitor.actions';
import {actionMonitorGroupsPageLoaded} from './monitor.actions';
import {actionMonitorRouteChangeLoaded} from './monitor.actions';
import {actionMonitorRouteMapFocus} from './monitor.actions';
import {actionMonitorRouteMapLoaded} from './monitor.actions';
import {actionMonitorRouteDetailsLoaded} from './monitor.actions';
import {actionMonitorRouteChangesLoaded} from './monitor.actions';
import {selectMonitorAdmin} from './monitor.selectors';

@Injectable()
export class MonitorEffects {

  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private router: Router,
              private appService: AppService,
              private mapService: MonitorRouteMapService,
              private longdistanceRouteMapService: LongdistanceRouteMapService) {
  }

  mapFocusEffect = createEffect(() =>
      this.actions$.pipe(
        ofType(actionMonitorRouteMapFocus),
        tap(action => this.mapService.focus(action.bounds))
      ),
    {dispatch: false}
  );

  monitorInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorGroupsPageInit),
      withLatestFrom(
        this.store.select(selectRouteParams),
        this.store.select(selectMonitorAdmin)
      ),
      mergeMap(([params, admin]) => {
        if (admin) {
          return this.appService.monitorAdminGroups().pipe(
            map(response => actionMonitorGroupsPageLoaded({response}))
          );
        }
        return this.appService.monitorGroups().pipe(
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
      mergeMap(([action, groupName]) => {
        return this.appService.monitorGroup(groupName).pipe(
          map(response => actionMonitorGroupPageLoaded({response}))
        );
      })
    )
  );

  monitorGroupChangesPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorGroupChangesPageInit),
      withLatestFrom(
        this.store.select(selectRouteParam('groupName'))
      ),
      mergeMap(([action, groupName]) => {
        return this.appService.monitorGroupChanges(groupName).pipe(
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
      mergeMap(([action, groupName]) => {
        return this.appService.monitorAdminRouteGroup(groupName).pipe(
          map(response => actionMonitorGroupDeleteLoaded({response}))
        );
      })
    )
  );

  monitorGroupUpdateInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorGroupUpdateInit),
      withLatestFrom(
        this.store.select(selectRouteParam('groupName'))
      ),
      mergeMap(([action, groupName]) => {
        return this.appService.monitorAdminRouteGroup(groupName).pipe(
          map(response => actionMonitorGroupUpdateLoaded({response}))
        );
      })
    )
  );

  monitorRouteDetailsInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteDetailsInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId'))
      ),
      mergeMap(([action, routeId]) => {
        return this.appService.monitorRoute(routeId).pipe(
          map(response => actionMonitorRouteDetailsLoaded({response}))
        );
      })
    )
  );

  monitorRouteMapInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteMapInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId'))
      ),
      mergeMap(([action, routeId]) => {
        return this.appService.monitorRouteMap(routeId).pipe(
          map(response => actionMonitorRouteMapLoaded({response}))
        );
      })
    )
  );

  monitorRouteChangesInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteChangesInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId'))
      ),
      mergeMap(([action, routeId]) => {
        return this.appService.monitorRouteChanges(routeId).pipe(
          map(response => actionMonitorRouteChangesLoaded({response}))
        );
      })
    )
  );

  monitorRouteChangeInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRouteChangeInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId')),
        this.store.select(selectRouteParam('changeSetId'))
      ),
      mergeMap(([action, routeId, changeSetId]) => {
        return this.appService.monitorRouteChange(routeId, changeSetId).pipe(
          map(response => actionMonitorRouteChangeLoaded({response}))
        );
      })
    )
  );

  addGroupEffect$ = createEffect(() =>
      this.actions$.pipe(
        ofType(actionMonitorGroupAdd),
        concatMap((action) => this.appService.monitorAdminAddRouteGroup(action.group)),
        tap(() => this.router.navigate(['/monitor']))
      ),
    {dispatch: false}
  );

  deleteGroupEffect$ = createEffect(() =>
      this.actions$.pipe(
        ofType(actionMonitorGroupDelete),
        concatMap((action) => this.appService.monitorAdminDeleteRouteGroup(action.groupName)),
        tap(() => this.router.navigate(['/monitor']))
      ),
    {dispatch: false}
  );

  updateGroupEffect$ = createEffect(() =>
      this.actions$.pipe(
        ofType(actionMonitorGroupUpdate),
        concatMap((action) => this.appService.monitorAdminUpdateRouteGroup(action.group)),
        tap(() => this.router.navigate(['/monitor']))
      ),
    {dispatch: false}
  );

  monitorChangesPageInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorChangesPageInit),
      mergeMap((action) => {
        return this.appService.monitorChanges().pipe(
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
      mergeMap((action) => {
        return this.appService.longdistanceRoutes().pipe(
          map(response => actionLongdistanceRoutesLoaded({response}))
        );
      })
    )
  );

  longdistanceRouteDetailsInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionLongdistanceRouteDetailsInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId'))
      ),
      mergeMap(([action, routeId]) => {
        return this.appService.longdistanceRoute(routeId).pipe(
          map(response => actionLongdistanceRouteDetailsLoaded({response}))
        );
      })
    )
  );

  longdistanceRouteMapInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionLongdistanceRouteMapInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId'))
      ),
      mergeMap(([action, routeId]) => {
        return this.appService.longdistanceRouteMap(routeId).pipe(
          map(response => actionLongdistanceRouteMapLoaded({response}))
        );
      })
    )
  );

  longdistanceRouteChangesInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionLongdistanceRouteChangesInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId'))
      ),
      mergeMap(([action, routeId]) => {
        return this.appService.longdistanceRouteChanges(routeId).pipe(
          map(response => actionLongdistanceRouteChangesLoaded({response}))
        );
      })
    )
  );

  longdistanceRouteChangeInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionLongdistanceRouteChangeInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId')),
        this.store.select(selectRouteParam('changeSetId'))
      ),
      mergeMap(([action, routeId, changeSetId]) => {
        return this.appService.longdistanceRouteChange(routeId, changeSetId).pipe(
          map(response => actionLongdistanceRouteChangeLoaded({response}))
        );
      })
    )
  );

}
