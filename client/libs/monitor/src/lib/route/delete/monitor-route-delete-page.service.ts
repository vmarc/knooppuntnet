import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
import { MonitorRouteDeletePageState } from './monitor-route-delete-page.state';
import { initialState } from './monitor-route-delete-page.state';

@Injectable()
export class MonitorRouteDeletePageService {
  private readonly _state = signal<MonitorRouteDeletePageState>(initialState);
  readonly state = this._state.asReadonly();

  constructor(
    private navService: NavService,
    private monitorService: MonitorService
  ) {
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
    this.monitorService.route(groupName, routeName).subscribe((response) => {
      const routeDescription = response.result?.routeDescription ?? description;
      this._state.update((state) => ({
        ...state,
        routeDescription,
        response,
      }));
    });
  }

  delete(): void {
    const groupName = this.state().groupName;
    const routeName = this.state().routeName;
    const url = `/monitor/groups/${groupName}`;
    this.monitorService
      .routeDelete(groupName, routeName)
      .subscribe(() => this.navService.go(url));
  }
}
