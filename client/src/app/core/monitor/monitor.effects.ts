import {Injectable} from '@angular/core';
import {Actions} from '@ngrx/effects';
import {createEffect} from '@ngrx/effects';
import {ofType} from '@ngrx/effects';
import {routerNavigatedAction} from '@ngrx/router-store';
import {Store} from '@ngrx/store';
import {tap} from 'rxjs/operators';
import {withLatestFrom} from 'rxjs/operators';
import {map} from 'rxjs/operators';
import {filter} from 'rxjs/operators';
import {mergeMap} from 'rxjs/operators';
import {AppService} from '../../app.service';
import {MonitorRouteMapService} from '../../monitor/route/map/monitor-route-map.service';
import {AppState} from '../core.state';
import {selectUrl} from '../core.state';
import {selectRouteParams} from '../core.state';
import {actionMonitorRouteChangeLoaded} from './monitor.actions';
import {actionMonitorRouteMapFocus} from './monitor.actions';
import {actionMonitorRouteMapLoaded} from './monitor.actions';
import {actionMonitorRoutesLoaded} from './monitor.actions';
import {actionMonitorRouteDetailsLoaded} from './monitor.actions';
import {actionMonitorRouteChangesLoaded} from './monitor.actions';

@Injectable()
export class MonitorEffects {

  constructor(private actions$: Actions,
              private store: Store<AppState>,
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
        this.store.select(selectRouteParams)
      ),
      filter(([action, url, params]) => url.startsWith('/monitor/routes')),
      mergeMap(([action, url, params]) => {
        if (url.endsWith('/routes')) {
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
      })
    )
  );

}
