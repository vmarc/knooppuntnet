import { HttpResourceRef } from '@angular/common/http';
import { effect } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorRouteSummary } from '@api/common/monitor/monitor-route-summary';
import { ApiResponse } from '@api/custom/api-response';
import { NavService } from '@app/shared/components/nav.service';

const emptySummary = {
  adminUser: false,
  groupName: '',
  routeName: '',
  routeDescription: '',
  routeId: '',
  relationId: 0,
  relationIds: [],
  memberCount: 0,
  segmentCount: 0,
  deviationCount: 0,
  bounds: undefined,
};

@Injectable({
  providedIn: 'root',
})
export class MonitorRouteService {
  private readonly _pageName = signal<string>(undefined);
  readonly pageName = this._pageName.asReadonly();

  private readonly _routeNotFound = signal<boolean>(false);
  readonly routeNotFound = this._routeNotFound.asReadonly();

  private readonly _summary = signal<MonitorRouteSummary>(emptySummary);
  readonly summary = this._summary.asReadonly();

  initPage(nav: NavService): void {
    const groupName = nav.param('groupName');
    const routeName = nav.param('routeName');
    const routeDescription = nav.state('description');
    if (this.summary().groupName === groupName && this.summary().routeName === routeName) {
      return;
    }
    const summary: MonitorRouteSummary = {
      ...emptySummary,
      groupName: groupName,
      routeName: routeName,
      routeDescription: routeDescription,
    };

    this._summary.set(summary);
  }

  request(
    pageName: string,
    action: () => HttpResourceRef<ApiResponse<any>>
  ): HttpResourceRef<ApiResponse<any>> {
    this._pageName.set(pageName);
    const response = action();
    effect(() => {
      if (response.hasValue()) {
        const result = response.value()?.result;
        if (result) {
          this.update(result.routeInfo);
        } else {
          this.updateRouteNotFound(true);
        }
      }
    });
    return response;
  }

  update(summary: MonitorRouteSummary): void {
    this._summary.set(summary);
  }

  updateRouteNotFound(value: boolean): void {
    this._routeNotFound.set(value);
  }
}
