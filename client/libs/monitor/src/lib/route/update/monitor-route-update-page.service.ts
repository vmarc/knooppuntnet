import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
import { initialState } from './monitor-route-update-page.state';
import { MonitorRouteUpdatePageState } from './monitor-route-update-page.state';

@Injectable()
export class MonitorRouteUpdatePageService {
  readonly #navService = inject(NavService);
  readonly #monitorService = inject(MonitorService);

  readonly #state = signal<MonitorRouteUpdatePageState>(initialState);
  readonly state = this.#state.asReadonly();

  constructor() {
    const groupName = this.#navService.param('groupName');
    const routeName = this.#navService.param('routeName');
    const description = this.#navService.state('description');
    const groupLink = `/monitor/groups/${groupName}`;
    this.#state.update((state) => ({
      ...state,
      groupName,
      routeName,
      routeDescription: description,
      groupLink,
    }));

    this.#monitorService
      .routeUpdatePage(groupName, routeName)
      .subscribe((response) => {
        const routeDescription =
          response.result?.routeDescription ?? description;
        this.#state.update((state) => ({
          ...state,
          routeDescription,
          response,
        }));
      });
  }
}
