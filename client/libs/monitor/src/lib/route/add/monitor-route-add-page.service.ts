import { Injectable } from '@angular/core';
import { MonitorRouteAddPage } from '@api/common/monitor';
import { Util } from '@app/components/shared';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorRouteAddPageService {
  private readonly _apiResponse = Util.response<MonitorRouteAddPage>();
  private readonly _groupDescription = this.nav.state('description');

  readonly groupName = this.nav.param('groupName');
  readonly groupDescription = this._groupDescription.asReadonly();
  readonly apiResponse = this._apiResponse.asReadonly();

  constructor(
    private monitorService: MonitorService,
    private nav: NavService
  ) {}

  init() {
    this.monitorService.routeAddPage(this.groupName()).subscribe((response) => {
      this._apiResponse.set(response);
      if (response.result) {
        this._groupDescription.set(response.result.groupDescription);
      }
    });
  }
}
