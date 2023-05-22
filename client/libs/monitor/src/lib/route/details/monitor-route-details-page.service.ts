import { Injectable } from '@angular/core';
import { MonitorRouteDetailsPage } from '@api/common/monitor';
import { Util } from '@app/components/shared';
import { NavService } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorRouteDetailsPageService {
  private readonly _apiResponse = Util.response<MonitorRouteDetailsPage>();
  private readonly _routeDescription = this.nav.state('description');

  readonly admin = this.monitorService.admin;
  readonly groupName = this.nav.param('groupName');
  readonly routeName = this.nav.param('routeName');
  readonly routeDescription = this._routeDescription.asReadonly();
  readonly apiResponse = this._apiResponse.asReadonly();

  constructor(private nav: NavService, private monitorService: MonitorService) {
    this.monitorService
      .route(this.groupName(), this.routeName())
      .subscribe((response) => {
        this._apiResponse.set(response);
        if (response.result) {
          this._routeDescription.set(response.result.routeDescription);
        }
      });
  }
}
