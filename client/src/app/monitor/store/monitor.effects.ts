import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { MonitorChangesParameters } from '@api/common/monitor/monitor-changes-parameters';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { concatMap } from 'rxjs/operators';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { selectRouteParam } from '../../core/core.state';
import { selectRouteParams } from '../../core/core.state';
import { selectPreferencesPageSize } from '../../core/preferences/preferences.selectors';
import { selectPreferencesImpact } from '../../core/preferences/preferences.selectors';
import { MonitorService } from '../monitor.service';
import { actionMonitorGroupPageLoad } from './monitor.actions';
import { actionMonitorRouteAddPageLoad } from './monitor.actions';
import { actionMonitorRouteDetailsPageLoad } from './monitor.actions';
import { actionMonitorRouteAnalyzed } from './monitor.actions';
import { actionMonitorRouteUploaded } from './monitor.actions';
import { actionMonitorRouteSaved } from './monitor.actions';
import { actionMonitorRouteUploadInit } from './monitor.actions';
import { actionMonitorRouteUpdatePageLoaded } from './monitor.actions';
import { actionMonitorRouteUpdatePageInit } from './monitor.actions';
import { actionMonitorRouteAddPageLoaded } from './monitor.actions';
import { actionMonitorRouteAddPageInit } from './monitor.actions';
import { actionMonitorRouteDelete } from './monitor.actions';
import { actionMonitorRouteDeletePageInit } from './monitor.actions';
import { actionMonitorRouteSaveInit } from './monitor.actions';
import { actionMonitorRouteInfoLoaded } from './monitor.actions';
import { actionMonitorRouteInfo } from './monitor.actions';
import { actionMonitorChangesPageIndex } from './monitor.actions';
import { actionMonitorRouteChangesPageIndex } from './monitor.actions';
import { actionMonitorGroupChangesPageIndex } from './monitor.actions';
import { actionMonitorChangesPageLoaded } from './monitor.actions';
import { actionMonitorChangesPageInit } from './monitor.actions';
import { actionMonitorGroupChangesPageLoaded } from './monitor.actions';
import { actionMonitorGroupChangesPageInit } from './monitor.actions';
import { actionMonitorGroupPageLoaded } from './monitor.actions';
import { actionMonitorGroupPageInit } from './monitor.actions';
import { actionMonitorGroupDeleteInit } from './monitor.actions';
import { actionMonitorGroupUpdateInit } from './monitor.actions';
import { actionMonitorRouteDetailsPageInit } from './monitor.actions';
import { actionMonitorRouteChangesPageInit } from './monitor.actions';
import { actionMonitorRouteChangePageInit } from './monitor.actions';
import { actionMonitorGroupsPageInit } from './monitor.actions';
import { actionMonitorGroupUpdateLoaded } from './monitor.actions';
import { actionMonitorGroupUpdate } from './monitor.actions';
import { actionMonitorGroupDelete } from './monitor.actions';
import { actionMonitorGroupDeleteLoaded } from './monitor.actions';
import { actionMonitorGroupAdd } from './monitor.actions';
import { actionMonitorGroupsPageLoaded } from './monitor.actions';
import { actionMonitorRouteChangePageLoaded } from './monitor.actions';
import { actionMonitorRouteDetailsPageLoaded } from './monitor.actions';
import { actionMonitorRouteChangesPageLoaded } from './monitor.actions';
import { selectMonitorGroupName } from './monitor.selectors';
import { selectMonitorChangesPageIndex } from './monitor.selectors';
import { selectMonitorGroupChangesPageIndex } from './monitor.selectors';
import { selectMonitorAdmin } from './monitor.selectors';

@Injectable()
export class MonitorEffects {
  // noinspection JSUnusedGlobalSymbols
  monitorGroupsPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorGroupsPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParams),
        this.store.select(selectMonitorAdmin),
      ]),
      mergeMap(([_, admin]) => {
        if (admin) {
          return this.monitorService
            .groups()
            .pipe(map((response) => actionMonitorGroupsPageLoaded(response)));
        }
        return this.monitorService
          .groups()
          .pipe(map((response) => actionMonitorGroupsPageLoaded(response)));
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorGroupPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorGroupPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('groupName'))),
      map(([_, groupName]) => actionMonitorGroupPageLoad({ groupName }))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorGroupPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorGroupPageLoad),
      mergeMap(({ groupName }) =>
        this.monitorService
          .group(groupName)
          .pipe(map((response) => actionMonitorGroupPageLoaded(response)))
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorGroupChangesPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(
        actionMonitorGroupChangesPageInit,
        actionMonitorGroupChangesPageIndex
      ),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('groupName')),
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectMonitorGroupChangesPageIndex),
        this.store.select(selectPreferencesImpact),
      ]),
      mergeMap(([_, groupName, pageSize, pageIndex, impact]) => {
        const parameters: MonitorChangesParameters = {
          pageSize,
          pageIndex,
          impact,
        };
        return this.monitorService
          .groupChanges(groupName, parameters)
          .pipe(
            map((response) => actionMonitorGroupChangesPageLoaded(response))
          );
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorGroupDeleteInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorGroupDeleteInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('groupName'))),
      mergeMap(([_, groupName]) =>
        this.monitorService
          .group(groupName)
          .pipe(map((response) => actionMonitorGroupDeleteLoaded(response)))
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorGroupUpdateInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorGroupUpdateInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('groupName'))),
      mergeMap(([_, groupName]) =>
        this.monitorService
          .group(groupName)
          .pipe(map((response) => actionMonitorGroupUpdateLoaded(response)))
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteAddPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteAddPageInit),
      concatLatestFrom(() => this.store.select(selectRouteParam('groupName'))),
      map(([_, groupName]) => actionMonitorRouteAddPageLoad({ groupName }))
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteAddPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteAddPageLoad),
      mergeMap(({ groupName }) =>
        this.monitorService
          .routeAddPage(groupName)
          .pipe(map((response) => actionMonitorRouteAddPageLoaded(response)))
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteInfo = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteInfo),
      mergeMap((action) =>
        this.monitorService
          .routeInfo(action.relationId)
          .pipe(map((response) => actionMonitorRouteInfoLoaded(response)))
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteAdd = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteSaveInit),
      concatLatestFrom(() => this.store.select(selectMonitorGroupName)),
      mergeMap(([parameters, groupName]) => {
        const properties = parameters.properties;
        if (parameters.mode === 'add') {
          return this.monitorService.routeAdd(groupName, properties).pipe(
            map((response) => {
              if (parameters.referenceFile) {
                return actionMonitorRouteUploadInit(parameters);
              }
              return actionMonitorRouteSaved(response);
            })
          );
        }
        return this.monitorService
          .routeUpdate(groupName, parameters.initialProperties.name, properties)
          .pipe(
            map((response) => {
              if (
                properties.referenceType === 'gpx' &&
                properties.referenceFileChanged
              ) {
                return actionMonitorRouteUploadInit(parameters);
              }
              return actionMonitorRouteSaved(response);
            })
          );
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteGpxUploadInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteUploadInit),
      concatLatestFrom(() => [this.store.select(selectMonitorGroupName)]),
      mergeMap(([parameters, groupName]) => {
        const relationId = 0;
        return this.monitorService
          .routeGpxUpload(
            groupName,
            parameters.properties.name,
            parameters.referenceFile,
            relationId
          )
          .pipe(map(() => actionMonitorRouteUploaded(parameters)));
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteUpdatePage = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteUpdatePageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('groupName')),
        this.store.select(selectRouteParam('routeName')),
      ]),
      mergeMap(([_, groupName, routeName]) =>
        this.monitorService
          .routeUpdatePage(groupName, routeName)
          .pipe(map((response) => actionMonitorRouteUpdatePageLoaded(response)))
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteGpxUploaded = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteUploaded),
      concatLatestFrom(() => [this.store.select(selectMonitorGroupName)]),
      map(() => actionMonitorRouteAnalyzed())
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteDeleteInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteDeletePageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('groupName')),
        this.store.select(selectRouteParam('routeName')),
      ]),
      mergeMap(([_, groupName, routeName]) =>
        this.monitorService
          .route(groupName, routeName)
          .pipe(
            map((response) => actionMonitorRouteDetailsPageLoaded(response))
          )
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteDelete = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionMonitorRouteDelete),
        concatLatestFrom(() => [
          this.store.select(selectRouteParam('groupName')),
          this.store.select(selectRouteParam('routeName')),
        ]),
        mergeMap(([_, groupName, routeName]) =>
          this.monitorService
            .routeDelete(groupName, routeName)
            .pipe(
              tap(() => this.router.navigate([`/monitor/groups/${groupName}`]))
            )
        )
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  monitorRouteDetailsPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteDetailsPageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('groupName')),
        this.store.select(selectRouteParam('routeName')),
      ]),
      map(([_, groupName, routeName]) =>
        actionMonitorRouteDetailsPageLoad({ groupName, routeName })
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteDetailsPageLoad = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteDetailsPageLoad),
      mergeMap(({ groupName, routeName }) =>
        this.monitorService
          .route(groupName, routeName)
          .pipe(
            map((response) => actionMonitorRouteDetailsPageLoaded(response))
          )
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteChangesPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(
        actionMonitorRouteChangesPageInit,
        actionMonitorRouteChangesPageIndex
      ),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('monitorRouteId')),
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectMonitorGroupChangesPageIndex),
        this.store.select(selectPreferencesImpact),
      ]),
      mergeMap(([_, monitorRouteId, pageSize, pageIndex, impact]) => {
        const parameters: MonitorChangesParameters = {
          pageSize,
          pageIndex,
          impact,
        };
        return this.monitorService
          .routeChanges(monitorRouteId, parameters)
          .pipe(
            map((response) => actionMonitorRouteChangesPageLoaded(response))
          );
      })
    );
  });

  // noinspection JSUnusedGlobalSymbols
  monitorRouteChangePageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorRouteChangePageInit),
      concatLatestFrom(() => [
        this.store.select(selectRouteParam('monitorRouteId')),
        this.store.select(selectRouteParam('changeSetId')),
        this.store.select(selectRouteParam('replicationNumber')),
      ]),
      mergeMap(([_, monitorRouteId, changeSetId, replicationNumber]) =>
        this.monitorService
          .routeChange(monitorRouteId, changeSetId, replicationNumber)
          .pipe(map((response) => actionMonitorRouteChangePageLoaded(response)))
      )
    );
  });

  // noinspection JSUnusedGlobalSymbols
  addGroupEffect = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionMonitorGroupAdd),
        concatMap((properties) => this.monitorService.groupAdd(properties)),
        tap(() => this.router.navigate(['/monitor']))
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  deleteGroupEffect = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionMonitorGroupDelete),
        concatMap((action) => this.monitorService.groupDelete(action.groupId)),
        tap(() => this.router.navigate(['/monitor']))
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  updateGroupEffect = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionMonitorGroupUpdate),
        concatMap((action) =>
          this.monitorService.groupUpdate(action.groupId, action.properties)
        ),
        tap(() => this.router.navigate(['/monitor']))
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  monitorChangesPageInit = createEffect(() => {
    return this.actions$.pipe(
      ofType(actionMonitorChangesPageInit, actionMonitorChangesPageIndex),
      concatLatestFrom(() => [
        this.store.select(selectPreferencesPageSize),
        this.store.select(selectMonitorChangesPageIndex),
        this.store.select(selectPreferencesImpact),
      ]),
      mergeMap(([_, pageSize, pageIndex, impact]) => {
        const parameters: MonitorChangesParameters = {
          pageSize,
          pageIndex,
          impact,
        };
        return this.monitorService
          .changes(parameters)
          .pipe(map((response) => actionMonitorChangesPageLoaded(response)));
      })
    );
  });

  constructor(
    private actions$: Actions,
    private store: Store,
    private router: Router,
    private monitorService: MonitorService
  ) {}
}
