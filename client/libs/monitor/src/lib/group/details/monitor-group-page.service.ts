import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
import { MonitorGroupPageState } from './monitor-group-page.state';
import { initialState } from './monitor-group-page.state';

@Injectable()
export class MonitorGroupPageService {
  readonly #navService = inject(NavService);
  readonly #monitorService = inject(MonitorService);

  readonly #state = signal<MonitorGroupPageState>(initialState);
  readonly state = this.#state.asReadonly();
  readonly admin = this.#monitorService.admin;

  constructor() {
    const groupName = this.#navService.param('groupName');
    const groupDescription = this.#navService.state('description');
    this.#state.update((state) => ({
      ...state,
      groupName,
      groupDescription,
    }));

    this.#monitorService.group(groupName).subscribe((response) => {
      const groupDescription =
        response.result?.groupDescription ?? this.state().groupDescription;
      this.#state.update((state) => ({
        ...state,
        groupDescription,
        response,
      }));
    });
  }
}
