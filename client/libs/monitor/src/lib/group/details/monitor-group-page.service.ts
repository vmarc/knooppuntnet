import { Injectable } from '@angular/core';
import { MonitorGroupPage } from '@api/common/monitor';
import { NavService } from '@app/components/shared';
import { Util } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorGroupPageService {
  private readonly _groupDescription = this.nav.state('description');
  private readonly _apiResponse = Util.response<MonitorGroupPage>();

  readonly admin = this.monitorService.admin;
  readonly groupName = this.nav.param('groupName');
  readonly groupDescription = this._groupDescription.asReadonly();
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
}
