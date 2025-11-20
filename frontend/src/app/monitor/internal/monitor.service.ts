import { HttpParams } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { LOCALE_ID } from '@angular/core';
import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { ValidationErrors } from '@angular/forms';
import { AbstractControl } from '@angular/forms';
import { AsyncValidatorFn } from '@angular/forms';
import { MonitorChangesPage } from '@api/common/monitor/monitor-changes-page';
import { MonitorChangesParameters } from '@api/common/monitor/monitor-changes-parameters';
import { MonitorGroupChangesPage } from '@api/common/monitor/monitor-group-changes-page';
import { MonitorGroupPage } from '@api/common/monitor/monitor-group-page';
import { MonitorGroupProperties } from '@api/common/monitor/monitor-group-properties';
import { MonitorGroupsPage } from '@api/common/monitor/monitor-groups-page';
import { MonitorRouteAddPage } from '@api/common/monitor/monitor-route-add-page';
import { MonitorRouteChangePage } from '@api/common/monitor/monitor-route-change-page';
import { MonitorRouteChangesPage } from '@api/common/monitor/monitor-route-changes-page';
import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';
import { MonitorRouteDeviationsPage } from '@api/common/monitor/monitor-route-deviations-page';
import { MonitorRouteInfoPage } from '@api/common/monitor/monitor-route-info-page';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { MonitorRouteMembersPage } from '@api/common/monitor/monitor-route-members-page';
import { MonitorRouteSegmentsPage } from '@api/common/monitor/monitor-route-segments-page';
import { MonitorRouteUpdatePage } from '@api/common/monitor/monitor-route-update-page';
import { MonitorRouteGpxPage } from '@api/common/monitor/monitor-route-gpx-page';
import { ApiResponse } from '@api/custom/api-response';
import { of } from 'rxjs';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';
import { map } from 'rxjs/operators';

@Injectable()
export class MonitorService {
  public readonly locale: string = inject(LOCALE_ID);
  private readonly http = inject(HttpClient);
  private readonly _adminEnabled = signal(false);
  private readonly _adminUser = signal(false);

  readonly adminEnabled = this._adminEnabled.asReadonly();
  readonly adminUser = this._adminUser.asReadonly();

  setAdmin(value: boolean): void {
    this._adminEnabled.set(value);
  }

  groups(): Observable<ApiResponse<MonitorGroupsPage>> {
    const url = '/api/monitor/groups';
    return this.http.get<ApiResponse<MonitorGroupsPage>>(url).pipe(
      tap((response) => {
        if (response.result) {
          this._adminUser.set(response.result.adminUser);
        }
      })
    );
  }

  private groupNames(): Observable<ApiResponse<Array<string>>> {
    const url = '/api/monitor/group-names';
    return this.http.get(url);
  }

  group(groupName: string): Observable<ApiResponse<MonitorGroupPage>> {
    const url = `/api/monitor/groups/${groupName}`;
    return this.http.get<ApiResponse<MonitorGroupPage>>(url).pipe(
      tap((response) => {
        if (response.result) {
          this._adminUser.set(response.result.adminUser);
        }
      })
    );
  }

  groupAdd(properties: MonitorGroupProperties): Observable<void> {
    const url = `/api/monitor/groups`;
    return this.http.post<void>(url, properties);
  }

  groupUpdate(groupId: string, properties: MonitorGroupProperties): Observable<void> {
    const url = `/api/monitor/groups/${groupId}`;
    return this.http.put<void>(url, properties);
  }

  groupDelete(groupId: string): Observable<void> {
    const url = `/api/monitor/groups/${groupId}`;
    return this.http.delete<void>(url);
  }

  groupChanges(
    groupName: string,
    parameters: MonitorChangesParameters
  ): Observable<ApiResponse<MonitorGroupChangesPage>> {
    const url = `/api/monitor/groups/${groupName}/changes`;
    return this.http.post(url, parameters);
  }

  changes(parameters: MonitorChangesParameters): Observable<ApiResponse<MonitorChangesPage>> {
    const url = `/api/monitor/changes`;
    return this.http.post(url, parameters);
  }

  route(groupName: string, routeName: string): Observable<ApiResponse<MonitorRouteDetailsPage>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}`;
    return this.http
      .get<ApiResponse<MonitorRouteDetailsPage>>(url, { params: this.languageParams() })
      .pipe(
        tap((response) => {
          if (response.result) {
            this._adminUser.set(response.result.summary.adminUser);
          }
        })
      );
  }

  routeMembers(
    groupName: string,
    routeName: string
  ): Observable<ApiResponse<MonitorRouteMembersPage>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}/members`;
    return this.http
      .get<ApiResponse<MonitorRouteMembersPage>>(url, { params: this.languageParams() })
      .pipe(
        tap((response) => {
          if (response.result) {
            this._adminUser.set(response.result.summary.adminUser);
          }
        })
      );
  }

  routeSegments(
    groupName: string,
    routeName: string
  ): Observable<ApiResponse<MonitorRouteSegmentsPage>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}/segments`;
    return this.http
      .get<ApiResponse<MonitorRouteSegmentsPage>>(url, { params: this.languageParams() })
      .pipe(
        tap((response) => {
          if (response.result) {
            this._adminUser.set(response.result.summary.adminUser);
          }
        })
      );
  }

  routeDeviations(
    groupName: string,
    routeName: string
  ): Observable<ApiResponse<MonitorRouteDeviationsPage>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}/deviations`;
    return this.http
      .get<ApiResponse<MonitorRouteDeviationsPage>>(url, { params: this.languageParams() })
      .pipe(
        tap((response) => {
          if (response.result) {
            this._adminUser.set(response.result.summary.adminUser);
          }
        })
      );
  }

  routeDelete(groupName: string, routeName: string): Observable<void> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}`;
    return this.http.delete<void>(url);
  }

  routeMap(
    groupName: string,
    routeName: string,
    subRelationIndex: number
  ): Observable<ApiResponse<MonitorRouteMapPage>> {
    let url = `/api/monitor/groups/${groupName}/routes/${routeName}/map`;
    if (subRelationIndex !== 0) {
      url = url + `/${subRelationIndex}`;
    }
    return this.http.get(url);
  }

  routeGpx(
    groupName: string,
    routeName: string,
    relationId: string
  ): Observable<ApiResponse<MonitorRouteGpxPage>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}/gpx/${relationId}`;
    return this.http.get(url);
  }

  routeChanges(
    groupName: string,
    routeName: string,
    parameters: MonitorChangesParameters
  ): Observable<ApiResponse<MonitorRouteChangesPage>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}/changes`;
    return this.http.post(url, parameters);
  }

  routeChange(
    groupName: string,
    routeName: string,
    changeSetId: string,
    replicationNumber: string
  ): Observable<ApiResponse<MonitorRouteChangePage>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}/changes/${changeSetId}/${replicationNumber}`;
    return this.http.get(url);
  }

  routeAddPage(groupName: string): Observable<ApiResponse<MonitorRouteAddPage>> {
    const url = `/api/monitor/route-add/${groupName}`;
    return this.http.get(url);
  }

  routeUpdatePage(
    groupName: string,
    routeName: string
  ): Observable<ApiResponse<MonitorRouteUpdatePage>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}/update-info`;
    return this.http.get(url);
  }

  routeInfo(relationId: number): Observable<ApiResponse<MonitorRouteInfoPage>> {
    const url = `/api/monitor/route-info/${relationId}`;
    return this.http.get(url);
  }

  asyncGroupNameUniqueValidator(initialGroupName: () => string): AsyncValidatorFn {
    return (c: AbstractControl): Observable<ValidationErrors> => {
      if (!c.value || c.value.length === 0 || c.value === initialGroupName()) {
        return of(null);
      } else {
        return this.groupNames().pipe(
          map((response) => response.result),
          map((groupNames) => {
            if (groupNames.includes(c.value)) {
              return { groupNameNonUnique: c.value };
            }
            return null;
          }),
          catchError(() => of(null))
        );
      }
    };
  }

  routeNames(groupName: string): Observable<ApiResponse<Array<string>>> {
    const url = `/api/monitor/groups/${groupName}/route-names`;
    return this.http.get(url);
  }

  private languageParams(): HttpParams {
    return new HttpParams().set('language', this.locale);
  }
}
