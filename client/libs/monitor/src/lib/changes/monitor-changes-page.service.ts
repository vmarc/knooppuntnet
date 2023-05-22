import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorChangesParameters } from '@api/common/monitor';
import { PreferencesService } from '@app/core';
import { MonitorService } from '../monitor.service';
import { initialState } from './monitor-changes-page.state';
import { MonitorChangesPageState } from './monitor-changes-page.state';

@Injectable()
export class MonitorChangesPageService {
  private readonly _state = signal<MonitorChangesPageState>(initialState);
  readonly state = this._state.asReadonly();
  readonly impact = this.preferencesService.impact;
  readonly pageSize = this.preferencesService.pageSize;

  constructor(
    private monitorService: MonitorService,
    private preferencesService: PreferencesService
  ) {
    this.load();
  }

  impactChanged(impact: boolean) {
    this.preferencesService.setImpact(impact);
    this.load();
  }

  pageSizeChanged(pageSize: number) {
    this.preferencesService.setPageSize(pageSize);
    this.load();
  }

  pageIndexChanged(pageIndex: number) {
    this._state.update((state) => ({ ...state, pageIndex }));
    this.load();
  }

  private load(): void {
    const parameters: MonitorChangesParameters = {
      pageSize: this.preferencesService.pageSize(),
      pageIndex: this._state().pageIndex,
      impact: this.preferencesService.impact(),
    };
    this.monitorService.changes(parameters).subscribe((response) => {
      this._state.update((state) => ({ ...state, response }));
    });
  }
}
