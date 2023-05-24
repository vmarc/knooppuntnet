import { HttpClient } from '@angular/common/http';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorRouteGpxPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';
import { NavService } from '@app/components/shared';
import { tap } from 'rxjs/operators';
import { MonitorRouteGpxState } from './monitor-route-gpx.state';
import { initialState } from './monitor-route-gpx.state';

@Injectable()
export class MonitorRouteGpxService {
  private readonly _state = signal<MonitorRouteGpxState>(initialState);
  readonly state = this._state.asReadonly();

  constructor(private navService: NavService, private http: HttpClient) {
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
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}/gpx/${subRelationId}`;
    this.http
      .get<ApiResponse<MonitorRouteGpxPage>>(url)
      .subscribe((response) => {
        this._state.update((state) => ({
          ...state,
          response,
        }));
      });
  }

  save(): void {
    console.log('TODO save');
  }

  delete(): void {
    const groupName = this.state().groupName;
    const routeName = this.state().routeName;
    const subRelationId = this.state().subRelationId;
    const routeUrl = `/monitor/groups/${groupName}/routes/${routeName}`;
    const apiUrl = `/api${routeUrl}/gpx/${subRelationId}`;
    this.http
      .delete(apiUrl)
      .pipe(tap(() => this.navService.go(routeUrl)))
      .subscribe();
  }
}
