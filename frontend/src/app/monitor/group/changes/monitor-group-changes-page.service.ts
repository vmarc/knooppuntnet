import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorChangesParameters } from '@api/common/monitor';
import { NavService } from '@app/components/shared';
import { State } from '@app/state';
import { MonitorService } from '../../monitor.service';
import { MonitorGroupChangesPageState } from './monitor-group-changes-page.state';
import { initialState } from './monitor-group-changes-page.state';

@Injectable()
export class MonitorGroupChangesPageService {
  private readonly state = inject(State);
  private readonly navService = inject(NavService);
  private readonly monitorService = inject(MonitorService);

  private readonly _changesState = signal<MonitorGroupChangesPageState>(initialState);
  readonly changesState = this._changesState.asReadonly();

  readonly impact = this.state.preferences.impact;

  constructor() {
    const groupName = this.navService.param('groupName');
    const groupDescription = this.navService.state('groupDescription');
    this._changesState.update((state) => ({
      ...state,
      groupName,
      groupDescription,
    }));
    this.load();
  }

  updateImpact(impact: boolean): void {
    this.state.preferences.updateImpact(impact);
    this.load();
  }

  updatePage(pageIndex: number) {
    this._changesState.update((state) => ({
      ...state,
      pageIndex,
    }));
    this.load();
  }

  private load(): void {
    const parameters: MonitorChangesParameters = {
      pageSize: this.state.preferences.pageSize(),
      pageIndex: this.changesState().pageIndex,
      impact: this.state.preferences.impact(),
    };
    this.monitorService
      .groupChanges(this.changesState().groupName, parameters)
      .subscribe((response) => {
        const groupDescription =
          response?.result?.groupDescription ?? this.changesState().groupDescription;
        this._changesState.update((state) => ({
          ...state,
          groupDescription,
          response,
        }));
      });
  }
}
