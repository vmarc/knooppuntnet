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
    const groupName = navService.newParam('groupName');
    const routeName = navService.newParam('routeName');
    const routeDescription = navService.newState('description');
    this._state.update((state) => ({
      ...state,
      groupName,
      routeName,
      routeDescription,
    }));

    this.monitorService
      .routeUpdatePage(groupName, routeName)
      .subscribe((response) => {
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
