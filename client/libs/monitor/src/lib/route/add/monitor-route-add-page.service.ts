import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
import { initialState } from './monitor-route-add-page.state';
import { MonitorRouteAddPageState } from './monitor-route-add-page.state';

@Injectable()
export class MonitorRouteAddPageService {
  readonly #monitorService = inject(MonitorService);
  readonly #navService = inject(NavService);

  readonly #state = signal<MonitorRouteAddPageState>(initialState);
  readonly state = this.#state.asReadonly();

  constructor() {
    const groupName = this.#navService.param('groupName');
    const description = this.#navService.state('description');
    const groupLink = `/monitor/groups/${groupName}`;
    this.#state.update((state) => ({
      ...state,
      groupName,
      groupDescription: description,
      groupLink,
    }));
    this.#monitorService.routeAddPage(groupName).subscribe((response) => {
      const groupDescription = response.result?.groupDescription ?? description;
      this.#state.update((state) => ({
        ...state,
        groupDescription,
        response,
      }));
    });
  }
}
