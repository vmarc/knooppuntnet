import { Injectable } from '@angular/core';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { filter } from 'rxjs/operators';
import { withLatestFrom } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { selectRouteParam } from '../../../core/core.state';
import { AppState } from '../../../core/core.state';
import { actionPreferencesPageSize } from '../../../core/preferences/preferences.actions';
import { actionPreferencesImpact } from '../../../core/preferences/preferences.actions';
import { selectPreferencesPageSize } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { actionNodeChangesFilterOption } from './node.actions';
import { actionNodeChangesPageIndex } from './node.actions';
import { actionNodeId } from './node.actions';
import { actionNodeMapPageInit } from './node.actions';
import { actionNodeChangesPageLoaded } from './node.actions';
import { actionNodeChangesPageInit } from './node.actions';
import { actionNodeDetailsPageInit } from './node.actions';
import { actionNodeMapPageLoaded } from './node.actions';
import { actionNodeDetailsPageLoaded } from './node.actions';
import { selectNodeChangesPage } from './node.selectors';
import { selectNodeChangesParameters } from './node.selectors';

@Injectable()
export class NodeEffects {
  nodeDetailsPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNodeDetailsPageInit),
      withLatestFrom(this.store.select(selectRouteParam('nodeId'))),
      mergeMap(([{}, nodeId]) => {
        this.store.dispatch(actionNodeId({ nodeId }));
        return this.appService
          .nodeDetails(nodeId)
          .pipe(map((response) => actionNodeDetailsPageLoaded({ response })));
      })
    )
  );

  nodeMapPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNodeMapPageInit),
      withLatestFrom(this.store.select(selectRouteParam('nodeId'))),
      mergeMap(([{}, nodeId]) => {
        this.store.dispatch(actionNodeId({ nodeId }));
        return this.appService
          .nodeMap(nodeId)
          .pipe(map((response) => actionNodeMapPageLoaded({ response })));
      })
    )
  );

  nodeChangesPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNodeChangesPageInit),
      withLatestFrom(
        this.store.select(selectRouteParam('nodeId')),
        this.store.select(selectPreferencesImpact),
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectNodeChangesParameters)
      ),
      mergeMap(
        ([
          {},
          nodeId,
          preferencesImpact,
          preferencesPageSize,
          urlChangesParameters,
        ]) => {
          const pageSize =
            urlChangesParameters?.pageSize ?? preferencesPageSize;
          const pageIndex = urlChangesParameters?.pageIndex ?? 0;
          const impact = urlChangesParameters?.impact ?? preferencesImpact;
          const changesParameters: ChangesParameters = {
            ...urlChangesParameters,
            pageSize,
            pageIndex,
            impact,
          };
          this.store.dispatch(actionNodeId({ nodeId }));
          return this.appService
            .nodeChanges(nodeId, changesParameters)
            .pipe(map((response) => actionNodeChangesPageLoaded({ response })));
        }
      )
    )
  );

  nodeChangesPageUpdate = createEffect(() =>
    this.actions$.pipe(
      ofType(
        actionNodeChangesPageIndex,
        actionPreferencesImpact,
        actionPreferencesPageSize,
        actionNodeChangesFilterOption
      ),
      withLatestFrom(this.store.select(selectNodeChangesPage)),
      // continue only if we are currently on the changes page!!
      filter(([{}, response]) => !!response),
      map(([{}, {}]) => {
        return actionNodeChangesPageInit();
      })
    )
  );

  constructor(
    private actions$: Actions,
    private store: Store<AppState>,
    private appService: AppService
  ) {}
}
