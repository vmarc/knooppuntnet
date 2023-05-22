import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
import { MonitorRouteChangePageState } from './monitor-route-change-page.state';
import { initialState } from './monitor-route-change-page.state';

@Injectable()
export class MonitorRouteChangePageService {
  private readonly _state = signal<MonitorRouteChangePageState>(initialState);
  readonly state = this._state.asReadonly();

  constructor(private nav: NavService, private monitorService: MonitorService) {
    const groupName = this.nav.newParam('groupName');
    const routeName = this.nav.newParam('routeName');
    const routeDescription = this.nav.newParam('routeDescription');
    const changeSetId = this.nav.newParam('changeSetId');
    const replicationNumber = this.nav.newParam('replicationNumber');
    this._state.update((state) => ({
      ...state,
      groupName,
      routeName,
      routeDescription,
      changeSetId,
      replicationNumber,
    }));
    this.monitorService
      .routeChange(groupName, routeName, changeSetId, replicationNumber)
      .subscribe((response) => {
        const routeDescription = 'TODO'; // TODO response.result?.routeDescription;
        this._state.update((state) => ({
          ...state,
          routeDescription,
          response,
        }));
      });
  }
}
