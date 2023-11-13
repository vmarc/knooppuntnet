import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
import { MonitorRouteChangePageState } from './monitor-route-change-page.state';
import { initialState } from './monitor-route-change-page.state';

@Injectable()
export class MonitorRouteChangePageService {
  private readonly navService = inject(NavService);
  private readonly monitorService = inject(MonitorService);

  private readonly _state = signal<MonitorRouteChangePageState>(initialState);
  readonly state = this._state.asReadonly();

  constructor() {
    const groupName = this.navService.param('groupName');
    const routeName = this.navService.param('routeName');
    const changeSetId = this.navService.param('changeSetId');
    const replicationNumber = this.navService.param('replicationNumber');
    const description = this.navService.state('description');
    this._state.update((state) => ({
      ...state,
      groupName,
      routeName,
      changeSetId,
      replicationNumber,
      routeDescription: description,
    }));
    this.monitorService
      .routeChange(groupName, routeName, changeSetId, replicationNumber)
      .subscribe((response) => {
        const routeDescription = 'TODO'; // TODO response.result?.routeDescription && description;
        this._state.update((state) => ({
          ...state,
          routeDescription,
          response,
        }));
      });
  }
}
