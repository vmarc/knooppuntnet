import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { NavService } from '@app/components/shared';
import { tap } from 'rxjs/operators';
import { MonitorService } from '../../monitor.service';
import { initialState } from './monitor-group-delete-page.state';
import { MonitorGroupDeletePageState } from './monitor-group-delete-page.state';

@Injectable()
export class MonitorGroupDeletePageService {
  private readonly nav = inject(NavService);
  private readonly monitorService = inject(MonitorService);

  private readonly _state = signal<MonitorGroupDeletePageState>(initialState);
  readonly state = this._state.asReadonly();

  constructor() {
    const groupName = this.nav.param('groupName');
    this._state.update((state) => ({
      ...state,
      groupName,
    }));
    this.monitorService.group(groupName).subscribe((response) =>
      this._state.update((state) => ({
        ...state,
        response,
      }))
    );
  }

  delete(groupId: string): void {
    this.monitorService
      .groupDelete(groupId)
      .pipe(
        tap((response) => {
          this.nav.go('/monitor');
        })
      )
      .subscribe();
  }
}
