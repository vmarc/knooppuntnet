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
import {ChangesParameters} from '../../../kpn/api/common/changes/filter/changes-parameters';
import {selectUrl} from '../../core.state';
import {selectRouteParams} from '../../core.state';
import {AppState} from '../../core.state';
import {actionNetworkRoutesLoaded} from './network.actions';
import {actionNetworkFactsLoaded} from './network.actions';
import {actionNetworkMapLoaded} from './network.actions';
import {actionNetworkChangesLoaded} from './network.actions';
import {actionNetworkDetailsLoaded} from './network.actions';
import {actionNetworkNodesLoaded} from './network.actions';

@Injectable()
export class NetworkEffects {
  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private appService: AppService) {
  }

  subsetPageEnter = createEffect(() =>
    this.actions$.pipe(
      ofType(routerNavigatedAction),
      withLatestFrom(
        this.store.select(selectUrl),
        this.store.select(selectRouteParams)
      ),
      filter(([action, url, params]) => url.startsWith('/analysis/network/')),
      mergeMap(([action, url, params]) => {
        const networkId: number = params['networkId'];

        if (url.endsWith('/nodes')) {
          return this.appService.networkNodes(networkId).pipe(
            map(response => actionNetworkNodesLoaded({response}))
          );
        }

        if (url.endsWith('/routes')) {
          return this.appService.networkRoutes(networkId).pipe(
            map(response => actionNetworkRoutesLoaded({response}))
          );
        }

        if (url.endsWith('/facts')) {
          return this.appService.networkFacts(networkId).pipe(
            map(response => actionNetworkFactsLoaded({response}))
          );
        }

        if (url.endsWith('/map')) {
          return this.appService.networkMap(networkId).pipe(
            map(response => actionNetworkMapLoaded({response}))
          );
        }

        if (url.endsWith('/changes')) {
          const parameters: ChangesParameters = null;
          return this.appService.networkChanges(networkId, parameters).pipe(
            map(response => actionNetworkChangesLoaded({response}))
          );
        }

        return this.appService.networkDetails(networkId).pipe(
          map(response => actionNetworkDetailsLoaded({response}))
        );
      })
    )
  );

}
