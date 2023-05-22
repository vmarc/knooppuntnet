import { effect } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { Nav } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
import { MonitorGroupPageState } from './monitor-group-page.state';
import { initialState } from './monitor-group-page.state';

@Injectable()
export class MonitorGroupPageService {
  private readonly _state = signal<MonitorGroupPageState>(initialState);
  readonly state = this._state.asReadonly();
  readonly admin = this.monitorService.admin;

  constructor(private monitorService: MonitorService) {
    effect(() => console.log(['MonitorGroupPageState', this.state()]));
  }

  init(nav: Nav): void {
    const groupName = nav.param('groupName');
    const groupDescription = nav.state('description');
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
