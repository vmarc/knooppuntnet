import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { Timestamp } from '@api/custom';
import { NavService } from '@app/components/shared';
import { tap } from 'rxjs/operators';
import { MonitorService } from '../../monitor.service';
import { MonitorRouteGpxState } from './monitor-route-gpx.state';
import { initialState } from './monitor-route-gpx.state';

@Injectable()
export class MonitorRouteGpxService {
  private readonly navService = inject(NavService);
  private readonly monitorService = inject(MonitorService);

  private readonly _state = signal<MonitorRouteGpxState>(initialState);
  readonly state = this._state.asReadonly();

  constructor() {
    const groupName = this.navService.param('groupName');
    const routeName = this.navService.param('routeName');
    const subRelationId = this.navService.queryParam('sub-relation-id');
    const groupLink = `/monitor/groups/${groupName}`;
    const routeLink = `/monitor/groups/${groupName}/routes/${routeName}`;
    this._state.update((state) => ({
      ...state,
      groupName,
      routeName,
      subRelationId,
      groupLink,
      routeLink,
    }));

    this.monitorService
      .routeGpx(groupName, routeName, subRelationId)
      .subscribe((response) => {
        this._state.update((state) => ({
          ...state,
          response,
        }));
      });
  }

  save(file: File, referenceTimestamp: Timestamp): void {
    const groupName = this.state().groupName;
    const routeName = this.state().routeName;
    const routeUrl = `/monitor/groups/${groupName}/routes/${routeName}`;
    this.monitorService
      .routeSubRelationGpxUpload(
        this.state().groupName,
        this.state().routeName,
        this.state().subRelationId,
        file,
        referenceTimestamp
      )
      .pipe(tap(() => this.navService.go(routeUrl)))
      .subscribe();
  }

  delete(): void {
    const groupName = this.state().groupName;
    const routeName = this.state().routeName;
    const subRelationId = this.state().subRelationId;
    const routeUrl = `/monitor/groups/${groupName}/routes/${routeName}`;
    this.monitorService
      .deleteGpx(groupName, routeName, subRelationId)
      .pipe(tap(() => this.navService.go(routeUrl)))
      .subscribe();
  }
}
