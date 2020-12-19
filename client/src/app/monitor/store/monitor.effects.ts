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
import {MonitorRouteMapService} from '../route/map/monitor-route-map.service';
import {actionMonitorGroupDeleteInit} from './monitor.actions';
import {actionMonitorGroupUpdateInit} from './monitor.actions';
import {actionMonitorRoutesInit} from './monitor.actions';
import {actionMonitorRouteDetailsInit} from './monitor.actions';
import {actionMonitorRouteMapInit} from './monitor.actions';
import {actionMonitorRouteChangesInit} from './monitor.actions';
import {actionMonitorRouteChangeInit} from './monitor.actions';
import {actionMonitorInit} from './monitor.actions';
import {actionMonitorGroupUpdateLoaded} from './monitor.actions';
import {actionMonitorUpdateRouteGroup} from './monitor.actions';
import {actionMonitorDeleteRouteGroup} from './monitor.actions';
import {actionMonitorGroupDeleteLoaded} from './monitor.actions';
import {actionMonitorAddRouteGroup} from './monitor.actions';
import {actionMonitorLoaded} from './monitor.actions';
import {actionMonitorRouteChangeLoaded} from './monitor.actions';
import {actionMonitorRouteMapFocus} from './monitor.actions';
import {actionMonitorRouteMapLoaded} from './monitor.actions';
import {actionMonitorRoutesLoaded} from './monitor.actions';
import {actionMonitorRouteDetailsLoaded} from './monitor.actions';
import {actionMonitorRouteChangesLoaded} from './monitor.actions';
import {selectMonitorAdmin} from './monitor.selectors';

@Injectable()
export class MonitorEffects {

  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private router: Router,
              private appService: AppService,
              private mapService: MonitorRouteMapService) {
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
      ofType(actionMonitorInit),
      withLatestFrom(
        this.store.select(selectRouteParams),
        this.store.select(selectMonitorAdmin)
      ),
      mergeMap(([params, admin]) => {
        if (admin) {
          return this.appService.monitorAdminRouteGroups().pipe(
            map(response => actionMonitorLoaded({response}))
          );
        }
        return this.appService.monitorRouteGroups().pipe(
          map(response => actionMonitorLoaded({response}))
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

  monitorRoutesInit = createEffect(() =>
    this.actions$.pipe(
      ofType(actionMonitorRoutesInit),
      mergeMap((action) => {
        return this.appService.monitorRoutes().pipe(
          map(response => actionMonitorRoutesLoaded({response}))
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
        ofType(actionMonitorAddRouteGroup),
        concatMap((action) => this.appService.monitorAdminAddRouteGroup(action.group)),
        tap(() => this.router.navigate(['/monitor']))
      ),
    {dispatch: false}
  );

  deleteGroupEffect$ = createEffect(() =>
      this.actions$.pipe(
        ofType(actionMonitorDeleteRouteGroup),
        concatMap((action) => this.appService.monitorAdminDeleteRouteGroup(action.groupName)),
        tap(() => this.router.navigate(['/monitor']))
      ),
    {dispatch: false}
  );

  updateGroupEffect$ = createEffect(() =>
      this.actions$.pipe(
        ofType(actionMonitorUpdateRouteGroup),
        concatMap((action) => this.appService.monitorAdminUpdateRouteGroup(action.group)),
        tap(() => this.router.navigate(['/monitor']))
      ),
    {dispatch: false}
  );

}
