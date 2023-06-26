import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
import { MonitorRouteChangePageState } from './monitor-route-change-page.state';
import { initialState } from './monitor-route-change-page.state';

@Injectable()
export class MonitorRouteChangePageService {
  readonly #navService = inject(NavService);
  readonly #monitorService = inject(MonitorService);

  readonly #state = signal<MonitorRouteChangePageState>(initialState);
  readonly state = this.#state.asReadonly();

  constructor() {
    const groupName = this.#navService.param('groupName');
    const routeName = this.#navService.param('routeName');
    const changeSetId = this.#navService.param('changeSetId');
    const replicationNumber = this.#navService.param('replicationNumber');
    const description = this.#navService.state('description');
    this.#state.update((state) => ({
      ...state,
      groupName,
      routeName,
      changeSetId,
      replicationNumber,
      routeDescription: description,
    }));
    this.#monitorService
      .routeChange(groupName, routeName, changeSetId, replicationNumber)
      .subscribe((response) => {
        const routeDescription = 'TODO'; // TODO response.result?.routeDescription && description;
        this.#state.update((state) => ({
          ...state,
          routeDescription,
          response,
        }));
      });
  }
}
