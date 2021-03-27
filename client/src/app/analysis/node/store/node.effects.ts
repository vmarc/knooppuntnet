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
import {actionNodeMapPageInit} from './node.actions';
import {actionNodeChangesPageLoaded} from './node.actions';
import {actionNodeChangesPageInit} from './node.actions';
import {actionNodeDetailsPageInit} from './node.actions';
import {actionNodeMapPageLoaded} from './node.actions';
import {actionNodeDetailsPageLoaded} from './node.actions';

@Injectable()
export class NodeEffects {

  nodeDetailsPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNodeDetailsPageInit),
      withLatestFrom(
        this.store.select(selectRouteParam('nodeId'))
      ),
      mergeMap(([action, nodeId]) => this.appService.nodeDetails(nodeId).pipe(
          map(response => actionNodeDetailsPageLoaded({response}))
        ))
    )
  );

  nodeMapPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNodeMapPageInit),
      withLatestFrom(
        this.store.select(selectRouteParam('nodeId'))
      ),
      mergeMap(([action, nodeId]) => this.appService.nodeMap(nodeId).pipe(
          map(response => actionNodeMapPageLoaded({response}))
        ))
    )
  );

  nodeChangesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNodeChangesPageInit),
      withLatestFrom(
        this.store.select(selectRouteParam('nodeId'))
      ),
      mergeMap(([action, nodeId]) => this.appService.nodeChanges(nodeId, null /* TODO PARAMETERS */).pipe(
          map(response => actionNodeChangesPageLoaded({response}))
        ))
    )
  );

  constructor(private actions$: Actions,
              private store: Store<AppState>,
              private appService: AppService) {
  }

}
