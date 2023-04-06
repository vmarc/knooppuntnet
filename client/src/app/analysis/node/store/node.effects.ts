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
import { tap } from 'rxjs/operators';
import { AppService } from '@app/app.service';
import { PageParams } from '@app/base/page-params';
import { MapPosition } from '@app/components/ol/domain/map-position';
import { selectQueryParam } from '@app/core/core.state';
import { selectQueryParams } from '@app/core/core.state';
import { selectRouteParams } from '@app/core/core.state';
import { selectRouteParam } from '@app/core/core.state';
import { selectPreferencesPageSize } from '@app/core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '@app/core/preferences/preferences.selectors';
import { selectPreferencesNetworkType } from '@app/core/preferences/preferences.selectors';
import { actionNodeMapPageLoad } from './node.actions';
import { actionNodeDetailsPageLoad } from './node.actions';
import { actionNodeChangesPageIndex } from './node.actions';
import { actionNodeChangesPageSize } from './node.actions';
import { actionNodeChangesPageImpact } from './node.actions';
import { actionNodeChangesPageLoad } from './node.actions';
import { actionNodeChangesFilterOption } from './node.actions';
import { actionNodeMapPageInit } from './node.actions';
import { actionNodeChangesPageLoaded } from './node.actions';
import { actionNodeChangesPageInit } from './node.actions';
import { actionNodeDetailsPageInit } from './node.actions';
import { actionNodeMapPageLoaded } from './node.actions';
import { actionNodeDetailsPageLoaded } from './node.actions';
import { actionNodeMapLayerVisible } from './node.actions';
import { selectNodeId } from './node.selectors';
import { selectNodeChangesParameters } from './node.selectors';
import { selectNodeMapLayerStates } from './node.selectors';
import { NodeMapLayerService } from '@app/analysis/node/map/node-map-layer.service';
import { NetworkTypes } from '@app/kpn/common/network-types';

@Injectable()
export class NodeEffects {
  // noinspection JSUnusedGlobalSymbols
  nodeDetailsPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionNodeDetailsPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('nodeId'))),
      map(([_, nodeId]) => actionNodeDetailsPageLoad({ nodeId }))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  nodeDetailsPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionNodeDetailsPageLoad),
      mergeMap((action) => this.appService.nodeDetails(action.nodeId)),
      map((response) => actionNodeDetailsPageLoaded(response))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  nodeMapPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionNodeMapPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('nodeId')),
        this.store.select(selectQueryParam('position')),
      ]),
      map(([_, nodeId, mapPositionString]) => {
        const mapPositionFromUrl =
          MapPosition.fromQueryParam(mapPositionString);

        return actionNodeMapPageLoad({ nodeId, mapPositionFromUrl });
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  nodeMapPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionNodeMapPageLoad),
      concatLatestFrom(() => this.store.select(selectPreferencesNetworkType)),
      mergeMap(([action, defaultNetworkType]) => {
        return this.appService.nodeMap(action.nodeId).pipe(
          map((response) => {
            const mapLayerStates = this.nodeMapLayerService.buildLayers(
              response.result.nodeMapInfo,
              NetworkTypes.withName(defaultNetworkType)
            );
            return actionNodeMapPageLoaded({
              response,
              mapPositionFromUrl: action.mapPositionFromUrl,
              mapLayerStates,
            });
          })
        );
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  layersVisibility = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionNodeMapLayerVisible),
        concatLatestFrom(() => this.store.select(selectNodeMapLayerStates)),
        tap(([_, layerStates]) =>
          this.nodeMapLayerService.updateLayerVisibility(layerStates)
        )
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  nodeChangesPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionNodeChangesPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParams),
        this.store.select(selectQueryParams),
        this.store.select(selectPreferencesImpact),
        this.store.select(selectPreferencesPageSize),
      ]),
      map(
        ([
          _,
          routeParams,
          queryParams,
          preferencesImpact,
          preferencesPageSize,
        ]) => {
          const nodeId = routeParams['nodeId'];
          const queryParamsWrapper = new PageParams(queryParams);
          const changesParameters = queryParamsWrapper.changesParameters(
            preferencesImpact,
            preferencesPageSize
          );
          return actionNodeChangesPageLoad({ nodeId, changesParameters });
        }
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  nodeChangesPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(
        actionNodeChangesPageLoad,
        actionNodeChangesPageImpact,
        actionNodeChangesPageSize,
        actionNodeChangesPageIndex,
        actionNodeChangesFilterOption
      ),
      concatLatestFrom(() => [
        this.store.select(selectNodeId),
        this.store.select(selectNodeChangesParameters),
      ]),
      mergeMap(([_, nodeId, changesParameters]) => {
        const promise = this.navigate(changesParameters);
        return from(promise).pipe(
          mergeMap(() =>
            this.appService.nodeChanges(nodeId, changesParameters)
          ),
          map((response) => actionNodeChangesPageLoaded(response))
        );
      })
    );
  });

  constructor(
    private nodeMapLayerService: NodeMapLayerService,
    private actions$: Actions,
    private store: Store,
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
