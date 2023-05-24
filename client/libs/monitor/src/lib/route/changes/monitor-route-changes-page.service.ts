import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorChangesParameters } from '@api/common/monitor';
import { NavService } from '@app/components/shared';
import { PreferencesService } from '@app/core';
import { MonitorService } from '../../monitor.service';
import { initialState } from './monitor-route-changes-page.state';
import { MonitorRouteChangesPageState } from './monitor-route-changes-page.state';

@Injectable()
export class MonitorRouteChangesPageService {
  private readonly _state = signal<MonitorRouteChangesPageState>(initialState);
  readonly state = this._state.asReadonly();

  readonly impact = this.preferencesService.impact;
  readonly pageSize = this.preferencesService.pageSize;

  constructor(
    private navService: NavService,
    private monitorService: MonitorService,
    private preferencesService: PreferencesService
  ) {
    const groupName = this.navService.param('groupName');
    const routeName = this.navService.param('routeName');
    const description = this.navService.state('description');
    this._state.update((state) => ({
      ...state,
      groupName,
      routeName,
      routeDescription: description,
    }));
    this.load();
  }

  impactChanged(impact: boolean) {
    this.preferencesService.setImpact(impact);
    this.load();
  }

  pageChanged(pageIndex: number) {
    this._state.update((state) => ({ ...state, pageIndex }));
    this.load();
  }

  private load(): void {
    const parameters: MonitorChangesParameters = {
      pageSize: this.preferencesService.pageSize(),
      pageIndex: this.state().pageIndex,
      impact: this.preferencesService.impact(),
    };
    this.monitorService
      .routeChanges(this.state().groupName, this.state().routeName, parameters)
      .subscribe((response) => {
        const routeDescription = 'TODO'; // TODO response?.result?.routeDescription && this.state().routeDescription;
        this._state.update((state) => ({
          ...state,
          routeDescription,
          response,
        }));
      });
  }
}
