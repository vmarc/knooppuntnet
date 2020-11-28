import {Injectable} from '@angular/core';
import {Actions} from '@ngrx/effects';
import {createEffect} from '@ngrx/effects';
import {ofType} from '@ngrx/effects';
import {routerNavigatedAction} from '@ngrx/router-store';
import {Store} from '@ngrx/store';
import {withLatestFrom} from 'rxjs/operators';
import {map} from 'rxjs/operators';
import {filter} from 'rxjs/operators';
import {mergeMap} from 'rxjs/operators';
import {AppService} from '../../../app.service';
import {selectUrl} from '../../core.state';
import {selectRouteParams} from '../../core.state';
import {AppState} from '../../core.state';
import {actionRouteDetailsLoaded} from './route.actions';
import {actionRouteMapLoaded} from './route.actions';

@Injectable()
export class RouteEffects {
  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private appService: AppService) {
  }

  routePageEnter = createEffect(() =>
    this.actions$.pipe(
      ofType(routerNavigatedAction),
      withLatestFrom(
        this.store.select(selectUrl),
        this.store.select(selectRouteParams)
      ),
      filter(([action, url, params]) => url.startsWith('/analysis/route/')),
      mergeMap(([action, url, params]) => {
        const routeId = params['routeId'];
        if (url.endsWith('/map')) {
          return this.appService.routeMap(routeId).pipe(
            map(response => actionRouteMapLoaded({response}))
          );
        }
        // if (url.endsWith('/changes')) {
        //   return this.appService.routeChanges(nodeId, null /* TODO PARAMETERS */).pipe(
        //     map(response => actionRouteChangesLoaded({response}))
        //   );
        // }
        return this.appService.routeDetails(routeId).pipe(
          map(response => actionRouteDetailsLoaded({response}))
        );
      })
    )
  );

}
