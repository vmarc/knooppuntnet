import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
import { initialState } from './monitor-route-update-page.state';
import { MonitorRouteUpdatePageState } from './monitor-route-update-page.state';

@Injectable()
export class MonitorRouteUpdatePageService {
  private readonly _state = signal<MonitorRouteUpdatePageState>(initialState);
  readonly state = this._state.asReadonly();

  constructor(
    private navService: NavService,
    private monitorService: MonitorService
  ) {
    const groupName = navService.param('groupName');
    const routeName = navService.param('routeName');
    const description = navService.state('description');
    const groupLink = `/monitor/groups/${groupName}`;
    this._state.update((state) => ({
      ...state,
      groupName,
      routeName,
      routeDescription: description,
      groupLink,
    }));

    this.monitorService
      .routeUpdatePage(groupName, routeName)
      .subscribe((response) => {
        const routeDescription =
          response.result?.routeDescription ?? description;
        this._state.update((state) => ({
          ...state,
          routeDescription,
          response,
        }));
      });
  }
}
