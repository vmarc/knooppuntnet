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
import {ChangesParameters} from '@api/common/changes/filter/changes-parameters';
import {Subset} from '@api/custom/subset';
import {selectUrl} from '../../core.state';
import {selectRouteParams} from '../../core.state';
import {AppState} from '../../core.state';
import {actionSubsetChangesLoaded} from './subset.actions';
import {actionSubsetMapLoaded} from './subset.actions';
import {actionSubsetOrphanRoutesLoaded} from './subset.actions';
import {actionSubsetOrphanNodesLoaded} from './subset.actions';
import {actionSubsetFactsLoaded} from './subset.actions';
import {actionSubsetNetworksLoaded} from './subset.actions';

@Injectable()
export class SubsetEffects {
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
      filter(([action, url, params]) => url.startsWith('/analysis/subset/')),
      mergeMap(([action, url, params]) => {
        const subset: Subset = null;

        if (url.endsWith('/networks')) {
          return this.appService.subsetNetworks(subset).pipe(
            map(response => actionSubsetNetworksLoaded({response}))
          );
        }

        if (url.endsWith('/facts')) {
          return this.appService.subsetFacts(subset).pipe(
            map(response => actionSubsetFactsLoaded({response}))
          );
        }

        if (url.endsWith('/orphan-nodes')) {
          return this.appService.subsetOrphanNodes(subset).pipe(
            map(response => actionSubsetOrphanNodesLoaded({response}))
          );
        }

        if (url.endsWith('/orphan-routes')) {
          return this.appService.subsetOrphanRoutes(subset).pipe(
            map(response => actionSubsetOrphanRoutesLoaded({response}))
          );
        }

        if (url.endsWith('/map')) {
          return this.appService.subsetMap(subset).pipe(
            map(response => actionSubsetMapLoaded({response}))
          );
        }

        if (url.endsWith('/changes')) {
          const parameters: ChangesParameters = null;
          return this.appService.subsetChanges(subset, parameters).pipe(
            map(response => actionSubsetChangesLoaded({response}))
          );
        }
      })
    )
  );
}
