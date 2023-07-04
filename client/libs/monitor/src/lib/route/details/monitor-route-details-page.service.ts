import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
import { MonitorRouteDetailsPageState } from './monitor-route-details-page.state';
import { initialState } from './monitor-route-details-page.state';

@Injectable()
export class MonitorRouteDetailsPageService {
  private readonly nav = inject(NavService);
  private readonly monitorService = inject(MonitorService);

  private readonly _state = signal<MonitorRouteDetailsPageState>(initialState);
  readonly state = this._state.asReadonly();
  readonly admin = this.monitorService.admin;

  constructor() {
    const groupName = this.nav.param('groupName');
    const routeName = this.nav.param('routeName');
    const routeDescription = this.nav.state('description');
    this._state.update((state) => ({
      ...state,
      groupName,
      routeName,
      routeDescription,
    }));
    this.monitorService.route(groupName, routeName).subscribe((response) => {
      const routeDescription =
        response.result?.routeDescription ?? this.state().routeDescription;
      this._state.update((state) => ({
        ...state,
        routeDescription,
        response,
      }));
    });
  }
}
