import { Injectable } from '@angular/core';
import { MonitorRouteUpdatePage } from '@api/common/monitor';
import { Util } from '@app/components/shared';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorRouteUpdatePageService {
  private readonly _apiResponse = Util.response<MonitorRouteUpdatePage>();
  private readonly _routeDescription = this.nav.state('description');

  readonly groupName = this.nav.param('groupName');
  readonly routeName = this.nav.param('routeName');
  readonly routeDescription = this._routeDescription.asReadonly();
  readonly apiResponse = this._apiResponse.asReadonly();

  constructor(
    private nav: NavService,
    private monitorService: MonitorService
  ) {}

  init() {
    this.monitorService
      .routeUpdatePage(this.groupName(), this.routeName())
      .subscribe((response) => {
        this._apiResponse.set(response);
        if (response.result) {
          this._routeDescription.set(response.result.routeDescription);
        }
      });
  }
}
