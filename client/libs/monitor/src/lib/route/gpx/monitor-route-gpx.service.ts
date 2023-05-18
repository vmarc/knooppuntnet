import { HttpClient } from '@angular/common/http';
import { WritableSignal } from '@angular/core';
import { signal } from '@angular/core';
import { computed } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorRouteGpxPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';
import { NavService } from '@app/components/shared';
import { tap } from 'rxjs/operators';

@Injectable()
export class MonitorRouteGpxService {
  readonly groupName = this.nav.param('groupName');
  readonly routeName = this.nav.param('routeName');
  readonly subRelationId = this.nav.queryParam('sub-relation-id');
  readonly groupLink = computed(() => `/monitor/groups/${this.groupName}`);
  readonly routeLink = computed(
    () => `/monitor/groups/${this.groupName}/routes/${this.routeName}`
  );
  readonly apiResponse: WritableSignal<ApiResponse<MonitorRouteGpxPage> | null> =
    signal(null);

  constructor(private nav: NavService, private http: HttpClient) {}

  init(): void {
    const url = `/api/monitor/groups/${this.groupName}/routes/${this.routeName}/gpx/${this.subRelationId}`;
    this.http
      .get<ApiResponse<MonitorRouteGpxPage>>(url)
      .pipe(tap((response) => this.apiResponse.set(response)))
      .subscribe();
  }

  save(): void {
    console.log('TODO save');
  }

  delete(): void {
    const routeUrl = `/monitor/groups/${this.groupName}/routes/${this.routeName}`;
    const apiUrl = `/api${routeUrl}/gpx/${this.subRelationId}`;

    this.http
      .delete(apiUrl)
      .pipe(tap(() => this.nav.go(routeUrl)))
      .subscribe();
  }
}
