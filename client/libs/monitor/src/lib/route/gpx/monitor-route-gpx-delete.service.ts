import { HttpClient } from '@angular/common/http';
import { WritableSignal } from '@angular/core';
import { signal } from '@angular/core';
import { computed } from '@angular/core';
import { Injectable } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { MonitorRouteGpxPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';
import { tap } from 'rxjs/operators';
import { map } from 'rxjs/operators';

@Injectable()
export class MonitorRouteGpxDeleteService {
  readonly groupName = toSignal(
    this.route.paramMap.pipe(map((m) => m.get('groupName')))
  );
  readonly groupLink = computed(() => `/monitor/groups/${this.groupName()}`);
  readonly routeName = toSignal(
    this.route.paramMap.pipe(map((m) => m.get('routeName')))
  );
  readonly subRelationId = toSignal(
    this.route.queryParamMap.pipe(map((m) => m.get('sub-relation-id')))
  );

  readonly routeLink = computed(
    () => `/monitor/groups/${this.groupName()}/routes/${this.routeName()}`
  );
  readonly test = signal(0);
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
