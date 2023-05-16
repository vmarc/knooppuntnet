import { Injectable } from '@angular/core';
import { MonitorGroupsPage } from '@api/common/monitor';
import { Util } from '@app/components/shared';
import { MonitorService } from '../monitor.service';

@Injectable()
export class MonitorGroupsPageService {
  readonly admin = this.monitorService.admin;
  private readonly _apiResponse = Util.response<MonitorGroupsPage>();
  readonly apiResponse = this._apiResponse.asReadonly();

  constructor(private monitorService: MonitorService) {}

  init(): void {
    this.monitorService
      .groups()
      .subscribe((response) => this._apiResponse.set(response));
  }
}
