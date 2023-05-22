import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
import { MonitorGroupPageState } from './monitor-group-page.state';
import { initialState } from './monitor-group-page.state';

@Injectable()
export class MonitorGroupPageService {
  private readonly _state = signal<MonitorGroupPageState>(initialState);
  readonly state = this._state.asReadonly();
  readonly admin = this.monitorService.admin;

  constructor(navService: NavService, private monitorService: MonitorService) {
    const groupName = navService.newParam('groupName');
    const groupDescription = navService.newState('description');
    this._state.update((state) => ({
      ...state,
      groupName,
      groupDescription,
    }));

    this.monitorService.group(groupName).subscribe((response) => {
      const groupDescription =
        response.result?.groupDescription ?? this.state().groupDescription;
      this._state.update((state) => ({
        ...state,
        groupDescription,
        response,
      }));
    });
  }
}
