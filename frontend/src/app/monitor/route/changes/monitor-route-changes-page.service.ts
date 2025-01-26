import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorChangesParameters } from '@api/common/monitor';
import { NavService } from '@app/components/shared';
import { State } from '@app/state';
import { MonitorService } from '../../monitor.service';
import { initialState } from './monitor-route-changes-page.state';
import { MonitorRouteChangesPageState } from './monitor-route-changes-page.state';

@Injectable()
export class MonitorRouteChangesPageService {
  private readonly state = inject(State);
  private readonly navService = inject(NavService);
  private readonly monitorService = inject(MonitorService);

  private readonly _changesState = signal<MonitorRouteChangesPageState>(initialState);
  readonly changesState = this._changesState.asReadonly();

  readonly impact = this.state.preferences.impact;
  readonly pageSize = this.state.preferences.pageSize;

  constructor() {
    const groupName = this.navService.param('groupName');
    const routeName = this.navService.param('routeName');
    const description = this.navService.state('description');
    this._changesState.update((state) => ({
      ...state,
      groupName,
      routeName,
      routeDescription: description,
    }));
    this.load();
  }

  updateImpact(impact: boolean) {
    this.state.preferences.updateImpact(impact);
    this.load();
  }

  updatePage(pageIndex: number) {
    this._changesState.update((state) => ({ ...state, pageIndex }));
    this.load();
  }

  private load(): void {
    const parameters: MonitorChangesParameters = {
      pageSize: this.state.preferences.pageSize(),
      pageIndex: this.changesState().pageIndex,
      impact: this.state.preferences.impact(),
    };
    this.monitorService
      .routeChanges(this.changesState().groupName, this.changesState().routeName, parameters)
      .subscribe((response) => {
        const routeDescription = 'TODO'; // TODO response?.result?.routeDescription && this.state().routeDescription;
        this._changesState.update((state) => ({
          ...state,
          routeDescription,
          response,
        }));
      });
  }
}
