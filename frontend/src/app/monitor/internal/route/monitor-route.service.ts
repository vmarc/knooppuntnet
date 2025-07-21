import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorRouteSummary } from '@api/common/monitor/monitor-route-summary';
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

  update(summary: MonitorRouteSummary): void {
    this._summary.set(summary);
  }
}
