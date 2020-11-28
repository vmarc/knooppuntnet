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
import {LocationNodesParameters} from '../../../kpn/api/common/location/location-nodes-parameters';
import {LocationKey} from '../../../kpn/api/custom/location-key';
import {selectUrl} from '../../core.state';
import {selectRouteParams} from '../../core.state';
import {AppState} from '../../core.state';
import {actionLocationRoutesLoaded} from './location.actions';
import {actionLocationFactsLoaded} from './location.actions';
import {actionLocationMapLoaded} from './location.actions';
import {actionLocationChangesLoaded} from './location.actions';
import {actionLocationEditLoaded} from './location.actions';
import {actionLocationNodesLoaded} from './location.actions';

@Injectable()
export class LocationEffects {
  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private appService: AppService) {
  }

  locationPageEnter = createEffect(() =>
    this.actions$.pipe(
      ofType(routerNavigatedAction),
      withLatestFrom(
        this.store.select(selectUrl),
        this.store.select(selectRouteParams)
      ),
      filter(([action, url, params]) => url.startsWith('/analysis/location/')),
      mergeMap(([action, url, params]) => {
        const locationKey: LocationKey = null;
        const parameters: LocationNodesParameters = null;

        if (url.endsWith('/nodes')) {
          return this.appService.locationNodes(locationKey, parameters).pipe(
            map(response => actionLocationNodesLoaded({response}))
          );
        }

        if (url.endsWith('/routes')) {
          return this.appService.locationRoutes(locationKey, parameters).pipe(
            map(response => actionLocationRoutesLoaded({response}))
          );
        }

        if (url.endsWith('/facts')) {
          return this.appService.locationFacts(locationKey).pipe(
            map(response => actionLocationFactsLoaded({response}))
          );
        }

        if (url.endsWith('/map')) {
          return this.appService.locationMap(locationKey).pipe(
            map(response => actionLocationMapLoaded({response}))
          );
        }

        if (url.endsWith('/changes')) {
          return this.appService.locationChanges(locationKey, parameters).pipe(
            map(response => actionLocationChangesLoaded({response}))
          );
        }

        if (url.endsWith('/edit')) {
          return this.appService.locationEdit(locationKey).pipe(
            map(response => actionLocationEditLoaded({response}))
          );
        }
      })
    )
  );

}
