import {Injectable} from '@angular/core';
import {Actions} from '@ngrx/effects';
import {createEffect} from '@ngrx/effects';
import {ofType} from '@ngrx/effects';
import {Store} from '@ngrx/store';
import {withLatestFrom} from 'rxjs/operators';
import {map} from 'rxjs/operators';
import {mergeMap} from 'rxjs/operators';
import {AppService} from '../../../app.service';
import {selectRouteParam} from '../../../core/core.state';
import {AppState} from '../../../core/core.state';
import {actionRouteChangesPageLoaded} from './route.actions';
import {actionRouteChangesPageInit} from './route.actions';
import {actionRouteMapPageInit} from './route.actions';
import {actionRouteDetailsPageInit} from './route.actions';
import {actionRouteDetailsPageLoaded} from './route.actions';
import {actionRouteMapPageLoaded} from './route.actions';

@Injectable()
export class RouteEffects {

  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private appService: AppService) {
  }

  routeDetails = createEffect(() =>
    this.actions$.pipe(
      ofType(actionRouteDetailsPageInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId'))
      ),
      mergeMap(([action, routeId]) => this.appService.routeDetails(routeId).pipe(
          map(response => actionRouteDetailsPageLoaded({response}))
        ))
    )
  );

  routeMap = createEffect(() =>
    this.actions$.pipe(
      ofType(actionRouteMapPageInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId'))
      ),
      mergeMap(([action, routeId]) => this.appService.routeMap(routeId).pipe(
          map(response => actionRouteMapPageLoaded({response}))
        ))
    )
  );

  routeChanges = createEffect(() =>
    this.actions$.pipe(
      ofType(actionRouteChangesPageInit),
      withLatestFrom(
        this.store.select(selectRouteParam('routeId'))
      ),
      mergeMap(([action, routeId]) => this.appService.routeChanges(routeId, null /* TODO PARAMETERS */).pipe(
          map(response => actionRouteChangesPageLoaded({response}))
        ))
    )
  );

}
