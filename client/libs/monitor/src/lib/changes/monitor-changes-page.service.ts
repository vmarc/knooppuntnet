import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorChangesParameters } from '@api/common/monitor';
import { PreferencesService } from '@app/core';
import { MonitorService } from '../monitor.service';
import { initialState } from './monitor-changes-page.state';
import { MonitorChangesPageState } from './monitor-changes-page.state';

@Injectable()
export class MonitorChangesPageService {
  readonly #monitorService = inject(MonitorService);
  readonly #preferencesService = inject(PreferencesService);

  readonly #state = signal<MonitorChangesPageState>(initialState);
  readonly state = this.#state.asReadonly();
  readonly impact = this.#preferencesService.impact;
  readonly pageSize = this.#preferencesService.pageSize;

  constructor() {
    this.#load();
  }

  impactChanged(impact: boolean) {
    this.#preferencesService.setImpact(impact);
    this.#load();
  }

  pageSizeChanged(pageSize: number) {
    this.#preferencesService.setPageSize(pageSize);
    this.#load();
  }

  pageIndexChanged(pageIndex: number) {
    this.#state.update((state) => ({ ...state, pageIndex }));
    this.#load();
  }

  #load(): void {
    const parameters: MonitorChangesParameters = {
      pageSize: this.#preferencesService.pageSize(),
      pageIndex: this.#state().pageIndex,
      impact: this.#preferencesService.impact(),
    };
    this.#monitorService.changes(parameters).subscribe((response) => {
      this.#state.update((state) => ({ ...state, response }));
    });
  }
}
