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
import {AppService} from '../../app.service';
import {AppState} from '../core.state';
import {selectUrl} from '../core.state';
import {selectRouteParams} from '../core.state';
import {actionLongDistanceRouteMapLoaded} from './long-distance.actions';
import {actionLongDistanceRoutesLoaded} from './long-distance.actions';
import {actionLongDistanceRouteDetailsLoaded} from './long-distance.actions';
import {actionLongDistanceRouteChangesLoaded} from './long-distance.actions';

@Injectable()
export class LongDistanceEffects {
  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private appService: AppService) {
  }

  longDistancePageEnter = createEffect(() =>
    this.actions$.pipe(
      ofType(routerNavigatedAction),
      withLatestFrom(
        this.store.select(selectUrl),
        this.store.select(selectRouteParams)
      ),
      filter(([action, url, params]) => url.startsWith('/long-distance/routes')),
      mergeMap(([action, url, params]) => {
        if (url.endsWith('/routes')) {
          return this.appService.longDistanceRoutes().pipe(
            map(response => actionLongDistanceRoutesLoaded({response}))
          );
        }
        const routeId = params['routeId'];
        if (url.endsWith('/map')) {
          return this.appService.longDistanceRouteMap(routeId).pipe(
            map(response => actionLongDistanceRouteMapLoaded({response}))
          );
        }
        if (url.endsWith('/changes')) {
          return this.appService.longDistanceRouteChanges(routeId).pipe(
            map(response => actionLongDistanceRouteChangesLoaded({response}))
          );
        }
        return this.appService.longDistanceRoute(routeId).pipe(
          map(response => actionLongDistanceRouteDetailsLoaded({response}))
        );
      })
    )
  );

}
