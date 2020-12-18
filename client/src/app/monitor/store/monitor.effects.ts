import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Actions} from '@ngrx/effects';
import {createEffect} from '@ngrx/effects';
import {ofType} from '@ngrx/effects';
import {routerNavigatedAction} from '@ngrx/router-store';
import {Store} from '@ngrx/store';
import {concatMap} from 'rxjs/operators';
import {tap} from 'rxjs/operators';
import {withLatestFrom} from 'rxjs/operators';
import {map} from 'rxjs/operators';
import {filter} from 'rxjs/operators';
import {mergeMap} from 'rxjs/operators';
import {AppService} from '../../app.service';
import {AppState} from '../../core/core.state';
import {selectUrl} from '../../core/core.state';
import {selectRouteParams} from '../../core/core.state';
import {MonitorRouteMapService} from '../route/map/monitor-route-map.service';
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

  pageEnter = createEffect(() =>
    this.actions$.pipe(
      ofType(routerNavigatedAction),
      withLatestFrom(
        this.store.select(selectUrl),
        this.store.select(selectRouteParams),
        this.store.select(selectMonitorAdmin)
      ),
      filter(([action, url, params, admin]) => url.startsWith('/monitor') && !url.endsWith('/monitor/admin/groups/add')),
      mergeMap(([action, url, params, admin]) => {
        if (/\/monitor$/.test(url)) {
          if (admin) {
            return this.appService.monitorAdminRouteGroups().pipe(
              map(response => actionMonitorLoaded({response}))
            );
          }
          return this.appService.monitorRouteGroups().pipe(
            map(response => actionMonitorLoaded({response}))
          );
        }
        if (/\/monitor\/admin\/groups\/.*\/delete$/.test(url)) {
          const groupName = params['groupName'];
          return this.appService.monitorAdminRouteGroup(groupName).pipe(
            map(response => actionMonitorGroupDeleteLoaded({response}))
          );
        }
        if (/\/monitor\/admin\/groups\/.*$/.test(url)) {
          const groupName = params['groupName'];
          return this.appService.monitorAdminRouteGroup(groupName).pipe(
            map(response => actionMonitorGroupUpdateLoaded({response}))
          );
        }

        if (url.startsWith('/monitor/long-distance-routes')) {
          if (url.endsWith('/long-distance-routes')) {
            return this.appService.monitorRoutes().pipe(
              map(response => actionMonitorRoutesLoaded({response}))
            );
          }
          const routeId = params['routeId'];
          if (url.endsWith('/map')) {
            return this.appService.monitorRouteMap(routeId).pipe(
              map(response => actionMonitorRouteMapLoaded({response}))
            );
          }
          if (url.endsWith('/changes')) {
            return this.appService.monitorRouteChanges(routeId).pipe(
              map(response => actionMonitorRouteChangesLoaded({response}))
            );
          }
          if (url.includes('/changes/')) {
            const changeSetId = params['changeSetId'];
            return this.appService.monitorRouteChange(routeId, changeSetId).pipe(
              map(response => actionMonitorRouteChangeLoaded({response}))
            );
          }
          return this.appService.monitorRoute(routeId).pipe(
            map(response => actionMonitorRouteDetailsLoaded({response}))
          );
        }
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
