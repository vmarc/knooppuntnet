import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorService } from '../monitor.service';
import { initialState } from './monitor-groups-page.state';
import { MonitorGroupsPageState } from './monitor-groups-page.state';

@Injectable()
export class MonitorGroupsPageService {
  readonly #monitorService = inject(MonitorService);

  readonly #state = signal<MonitorGroupsPageState>(initialState);
  readonly state = this.#state.asReadonly();
  readonly admin = this.#monitorService.admin;

  constructor() {
    this.#monitorService
      .groups()
      .subscribe((response) =>
        this.#state.update((state) => ({ ...state, response }))
      );
  }
}
