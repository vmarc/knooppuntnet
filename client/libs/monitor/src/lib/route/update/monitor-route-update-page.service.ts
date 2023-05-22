import { signal } from '@angular/core';
import { effect } from '@angular/core';
import { Injectable } from '@angular/core';
import { Nav } from '@app/components/shared';
import { MonitorService } from '../../monitor.service';
import { initialState } from './monitor-route-update-page.state';
import { MonitorRouteUpdatePageState } from './monitor-route-update-page.state';

@Injectable()
export class MonitorRouteUpdatePageService {
  private readonly _state = signal<MonitorRouteUpdatePageState>(initialState);
  readonly state = this._state.asReadonly();
  // private readonly _apiResponse = Util.response<MonitorRouteUpdatePage>();
  // private readonly _routeDescription = this.nav.state('description');
  //
  // readonly groupName = this.nav.param('groupName');
  // readonly routeName = this.nav.param('routeName');
  // readonly routeDescription = this._routeDescription.asReadonly();
  // readonly apiResponse = this._apiResponse.asReadonly();

  constructor(private monitorService: MonitorService) {
    console.log('MonitorRouteUpdatePageService.constructor()');
    effect(() => console.log(['MonitorRouteUpdatePageState', this.state()]));
  }

  init(nav: Nav) {
    const groupName = nav.param('groupName');
    const routeName = nav.param('routeName');
    const routeDescription = nav.state('description');
    this._state.update((state) => ({
      ...state,
      groupName,
      routeName,
      routeDescription,
    }));

    this.monitorService
      .routeUpdatePage(groupName, routeName)
      .subscribe((response) => {
        const routeDescription =
          response.result?.routeDescription ?? this.state().routeDescription;
        this._state.update((state) => ({
          ...state,
          routeDescription,
          response,
        }));
      });
  }
}
