import { Injectable } from '@angular/core';
import { MonitorGroupPage } from '@api/common/monitor';
import { NavService } from '@app/components/shared';
import { Util } from '@app/components/shared';
import { tap } from 'rxjs/operators';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorGroupDeletePageService {
  private readonly _apiResponse = Util.response<MonitorGroupPage>();

  readonly groupName = this.nav.param('groupName');
  readonly apiResponse = this._apiResponse.asReadonly();

  constructor(
    private nav: NavService,
    private monitorService: MonitorService
  ) {}

  init(): void {
    this.monitorService
      .group(this.groupName())
      .subscribe((response) => this._apiResponse.set(response));
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
