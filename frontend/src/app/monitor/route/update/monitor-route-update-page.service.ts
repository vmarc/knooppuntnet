import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
import { initialState } from './monitor-route-update-page.state';
import { MonitorRouteUpdatePageState } from './monitor-route-update-page.state';

@Injectable()
export class MonitorRouteUpdatePageService {
  private readonly navService = inject(NavService);
  private readonly monitorService = inject(MonitorService);

  private readonly _state = signal<MonitorRouteUpdatePageState>(initialState);
  readonly state = this._state.asReadonly();

  constructor() {
    const groupName = this.navService.param('groupName');
    const routeName = this.navService.param('routeName');
    const description = this.navService.state('description');
    const groupLink = `/monitor/groups/${groupName}`;
    this._state.update((state) => ({
      ...state,
      groupName,
      routeName,
      routeDescription: description,
      groupLink,
    }));

    this.monitorService.routeUpdatePage(groupName, routeName).subscribe((response) => {
      const routeDescription = response.result?.routeDescription ?? description;
      this._state.update((state) => ({
        ...state,
        routeDescription,
        response,
      }));
    });
  }
}
