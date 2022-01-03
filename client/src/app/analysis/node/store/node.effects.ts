import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { Params } from '@angular/router';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { from } from 'rxjs';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { AppService } from '../../../app.service';
import { QueryParams } from '../../../base/query-params';
import { selectQueryParams } from '../../../core/core.state';
import { selectRouteParams } from '../../../core/core.state';
import { selectRouteParam } from '../../../core/core.state';
import { AppState } from '../../../core/core.state';
import { selectPreferencesPageSize } from '../../../core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '../../../core/preferences/preferences.selectors';
import { actionNodeChangesPageSize } from './node.actions';
import { actionNodeChangesPageImpact } from './node.actions';
import { actionNodeChangesPageLoad } from './node.actions';
import { actionNodeChangesFilterOption } from './node.actions';
import { actionNodeId } from './node.actions';
import { actionNodeMapPageInit } from './node.actions';
import { actionNodeChangesPageLoaded } from './node.actions';
import { actionNodeChangesPageInit } from './node.actions';
import { actionNodeDetailsPageInit } from './node.actions';
import { actionNodeMapPageLoaded } from './node.actions';
import { actionNodeDetailsPageLoaded } from './node.actions';
import { selectNodeId } from './node.selectors';
import { selectNodeChangesParameters } from './node.selectors';

@Injectable()
export class NodeEffects {
  nodeDetailsPage = createEffect(() =>
    this.actions$.pipe(
      ofType(actionNodeDetailsPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('nodeId'))),
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
      concatLatestFrom(() => this.store.select(selectRouteParam('nodeId'))),
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
      concatLatestFrom(() => [
        this.store.select(selectRouteParams),
        this.store.select(selectQueryParams),
        this.store.select(selectPreferencesImpact),
        this.store.select(selectPreferencesPageSize),
      ]),
      map(
        ([
          {},
          routeParams,
          queryParams,
          preferencesImpact,
          preferencesPageSize,
        ]) => {
          const nodeId = routeParams['nodeId'];
          const queryParamsWrapper = new QueryParams(queryParams);
          const changesParameters = queryParamsWrapper.changesParameters(
            preferencesImpact,
            preferencesPageSize
          );
          return actionNodeChangesPageLoad({ nodeId, changesParameters });
        }
      )
    )
  );

  nodeChangesPageLoad = createEffect(() =>
    this.actions$.pipe(
      ofType(
        actionNodeChangesPageLoad,
        actionNodeChangesPageImpact,
        actionNodeChangesPageSize,
        actionNodeChangesFilterOption
      ),
      concatLatestFrom(() => [
        this.store.select(selectNodeId),
        this.store.select(selectNodeChangesParameters),
      ]),
      mergeMap(([{}, nodeId, changesParameters]) => {
        const promise = this.navigate(changesParameters);
        return from(promise).pipe(
          mergeMap(() => {
            return this.appService
              .nodeChanges(nodeId, changesParameters)
              .pipe(
                map((response) => actionNodeChangesPageLoaded({ response }))
              );
          })
        );
      })
    )
  );

  constructor(
    private actions$: Actions,
    private store: Store<AppState>,
    private appService: AppService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  private navigate(changesParameters: ChangesParameters): Promise<boolean> {
    const queryParams: Params = {
      ...changesParameters,
    };
    return this.router.navigate([], {
      relativeTo: this.route,
      queryParams,
    });
  }
}
