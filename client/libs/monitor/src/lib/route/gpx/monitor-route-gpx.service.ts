import { HttpClient } from '@angular/common/http';
import { WritableSignal } from '@angular/core';
import { signal } from '@angular/core';
import { computed } from '@angular/core';
import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { MonitorRouteGpxPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';
import { Util } from '@app/components/shared';
import { tap } from 'rxjs/operators';

@Injectable()
export class MonitorRouteGpxService {
  readonly groupName = Util.param(this.route, 'groupName');
  readonly routeName = Util.param(this.route, 'routeName');
  readonly subRelationId = Util.queryParam(this.route, 'sub-relation-id');
  readonly groupLink = computed(() => `/monitor/groups/${this.groupName()}`);
  readonly routeLink = computed(
    () => `/monitor/groups/${this.groupName()}/routes/${this.routeName()}`
  );
  readonly apiResponse: WritableSignal<ApiResponse<MonitorRouteGpxPage> | null> =
    signal(null);

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  init(): void {
    const url = `/api/monitor/groups/${this.groupName()}/routes/${this.routeName()}/gpx/${this.subRelationId()}`;
    this.http
      .get<ApiResponse<MonitorRouteGpxPage>>(url)
      .pipe(tap((response) => this.apiResponse.set(response)))
      .subscribe();
  }

  save(): void {
    console.log('TODO save');
  }

  delete(): void {
    const routeUrl = `/monitor/groups/${this.groupName()}/routes/${this.routeName()}`;
    const apiUrl = `/api${routeUrl}/gpx/${this.subRelationId()}`;

    this.http
      .delete(apiUrl)
      .pipe(
        tap((response) => {
          // TODO handle error
          this.router.navigate([routeUrl]);
        })
      )
      .subscribe();
  }
}
