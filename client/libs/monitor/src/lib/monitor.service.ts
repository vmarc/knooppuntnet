import { HttpClient } from '@angular/common/http';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { ValidationErrors } from '@angular/forms';
import { AbstractControl } from '@angular/forms';
import { AsyncValidatorFn } from '@angular/forms';
import { MonitorChangesPage } from '@api/common/monitor';
import { MonitorChangesParameters } from '@api/common/monitor';
import { MonitorGroupChangesPage } from '@api/common/monitor';
import { MonitorGroupPage } from '@api/common/monitor';
import { MonitorGroupProperties } from '@api/common/monitor';
import { MonitorGroupsPage } from '@api/common/monitor';
import { MonitorRouteAddPage } from '@api/common/monitor';
import { MonitorRouteChangePage } from '@api/common/monitor';
import { MonitorRouteChangesPage } from '@api/common/monitor';
import { MonitorRouteDetailsPage } from '@api/common/monitor';
import { MonitorRouteInfoPage } from '@api/common/monitor';
import { MonitorRouteMapPage } from '@api/common/monitor';
import { MonitorRouteProperties } from '@api/common/monitor';
import { MonitorRouteSaveResult } from '@api/common/monitor';
import { MonitorRouteUpdatePage } from '@api/common/monitor';
import { MonitorRouteGpxPage } from '@api/common/monitor';
import { Timestamp } from '@api/custom';
import { ApiResponse } from '@api/custom';
import { of } from 'rxjs';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';
import { map } from 'rxjs/operators';

@Injectable()
export class MonitorService {
  private readonly _admin = signal(false);
  private readonly _adminRole = signal(false);

  readonly admin = this._admin.asReadonly();
  readonly adminRole = this._adminRole.asReadonly();

  constructor(private http: HttpClient) {}

  setAdmin(value: boolean): void {
    this._admin.set(value);
  }

  groups(): Observable<ApiResponse<MonitorGroupsPage>> {
    const url = '/api/monitor/groups';
    return this.http.get<ApiResponse<MonitorGroupsPage>>(url).pipe(
      tap((response) => {
        if (response.result) {
          this._adminRole.set(response.result.adminRole);
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
          this._adminRole.set(response.result.adminRole);
        }
      })
    );
  }

  groupAdd(properties: MonitorGroupProperties): Observable<void> {
    const url = `/api/monitor/groups`;
    return this.http.post<void>(url, properties);
  }

  groupUpdate(
    groupId: string,
    properties: MonitorGroupProperties
  ): Observable<void> {
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

  changes(
    parameters: MonitorChangesParameters
  ): Observable<ApiResponse<MonitorChangesPage>> {
    const url = `/api/monitor/changes`;
    return this.http.post(url, parameters);
  }

  routeAdd(
    groupName: string,
    properties: MonitorRouteProperties
  ): Observable<ApiResponse<MonitorRouteSaveResult>> {
    const url = `/api/monitor/groups/${groupName}`;
    return this.http.post(url, properties);
  }

  routeUpdate(
    groupName: string,
    routeName: string,
    properties: MonitorRouteProperties
  ): Observable<ApiResponse<MonitorRouteSaveResult>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}`;
    return this.http.put(url, properties);
  }

  route(
    groupName: string,
    routeName: string
  ): Observable<ApiResponse<MonitorRouteDetailsPage>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}`;
    return this.http.get<ApiResponse<MonitorRouteDetailsPage>>(url).pipe(
      tap((response) => {
        if (response.result) {
          this._adminRole.set(response.result.adminRole);
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
    relationId: number
  ): Observable<ApiResponse<MonitorRouteMapPage>> {
    let url = `/api/monitor/groups/${groupName}/routes/${routeName}/map`;
    if (relationId !== 0) {
      url = url + `/${relationId}`;
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

  deleteGpx(
    groupName: string,
    routeName: string,
    relationId: string
  ): Observable<void> {
    const routeUrl = `/monitor/groups/${groupName}/routes/${routeName}`;
    const apiUrl = `/api${routeUrl}/gpx/${relationId}`;
    return this.http.delete<void>(apiUrl);
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

  routeAddPage(
    groupName: string
  ): Observable<ApiResponse<MonitorRouteAddPage>> {
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

  routeGpxUpload(
    groupName: string,
    routeName: string,
    file: File
  ): Observable<void> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}/upload`;
    return this.http.post<void>(url, formData);
  }

  routeSubRelationGpxUpload(
    groupName: string,
    routeName: string,
    subRelationId: string,
    file: File,
    referenceTimestamp: Timestamp
  ): Observable<void> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('referenceTimestamp', referenceTimestamp);
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}/upload/${subRelationId}`;
    return this.http.post<void>(url, formData);
  }

  routeAnalyze(
    groupName: string,
    routeName: string
  ): Observable<ApiResponse<MonitorRouteSaveResult>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}/analyze`;
    return this.http.post(url, '');
  }

  asyncGroupNameUniqueValidator(
    initialGroupName: () => string
  ): AsyncValidatorFn {
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
}
