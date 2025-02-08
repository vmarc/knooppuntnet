import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorChangesParameters } from '@api/common/monitor/monitor-changes-parameters';
import { State } from '@app/state';
import { MonitorService } from '../monitor.service';
import { initialState } from './monitor-changes-page.state';
import { MonitorChangesPageState } from './monitor-changes-page.state';

@Injectable()
export class MonitorChangesPageService {
  private readonly state = inject(State);
  private readonly monitorService = inject(MonitorService);

  private readonly _changesState = signal<MonitorChangesPageState>(initialState);
  readonly changesState = this._changesState.asReadonly();
  readonly impact = this.state.preferences.impact;
  readonly pageSize = this.state.preferences.pageSize;

  constructor() {
    this.load();
  }

  updateImpact(impact: boolean) {
    this.state.preferences.updateImpact(impact);
    this.load();
  }

  updatePageSize(pageSize: number) {
    this.state.preferences.updatePageSize(pageSize);
    this.load();
  }

  updatePageIndex(pageIndex: number) {
    this._changesState.update((state) => ({ ...state, pageIndex }));
    this.load();
  }

  private load(): void {
    const parameters: MonitorChangesParameters = {
      pageSize: this.state.preferences.pageSize(),
      pageIndex: this._changesState().pageIndex,
      impact: this.state.preferences.impact(),
    };
    this.monitorService.changes(parameters).subscribe((response) => {
      this._changesState.update((state) => ({ ...state, response }));
    });
  }
}
