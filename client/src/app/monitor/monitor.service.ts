import { HttpClient } from '@angular/common/http';
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
import { MonitorRouteInfoPage } from '@api/common/monitor/monitor-route-info-page';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { MonitorRouteProperties } from '@api/common/monitor/monitor-route-properties';
import { MonitorRouteUpdatePage } from '@api/common/monitor/monitor-route-update-page';
import { ApiResponse } from '@api/custom/api-response';
import { Store } from '@ngrx/store';
import { of } from 'rxjs';
import { Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';
import { map } from 'rxjs/operators';
import { AppState } from '../core/core.state';
import { selectMonitorGroupName } from './store/monitor.selectors';

@Injectable()
export class MonitorService {
  constructor(private http: HttpClient, private store: Store<AppState>) {}

  groups(): Observable<ApiResponse<MonitorGroupsPage>> {
    const url = '/api/monitor/groups';
    return this.http.get(url);
  }

  private groupNames(): Observable<ApiResponse<Array<string>>> {
    const url = '/api/monitor/group-names';
    return this.http.get(url);
  }

  group(groupName: string): Observable<ApiResponse<MonitorGroupPage>> {
    const url = `/api/monitor/groups/${groupName}`;
    return this.http.get(url);
  }

  addGroup(properties: MonitorGroupProperties): Observable<any> {
    const url = `/api/monitor/groups`;
    return this.http.post(url, properties);
  }

  updateGroup(
    groupId: string,
    properties: MonitorGroupProperties
  ): Observable<any> {
    const url = `/api/monitor/groups/${groupId}`;
    return this.http.put(url, properties);
  }

  deleteGroup(groupId: string): Observable<any> {
    const url = `/api/monitor/groups/${groupId}`;
    return this.http.delete(url);
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

  addRoute(
    groupName: string,
    properties: MonitorRouteProperties
  ): Observable<ApiResponse<MonitorGroupPage>> {
    const url = `/api/monitor/groups/${groupName}`;
    return this.http.post(url, properties);
  }

  route(
    groupName: string,
    routeName: string
  ): Observable<ApiResponse<MonitorRouteDetailsPage>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}`;
    return this.http.get(url);
  }

  deleteRoute(
    groupName: string,
    routeName: string
  ): Observable<ApiResponse<void>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}`;
    return this.http.delete(url);
  }

  routeMap(
    groupName: string,
    routeName: string
  ): Observable<ApiResponse<MonitorRouteMapPage>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}/map`;
    return this.http.get(url);
  }

  routeChanges(
    monitorRouteId: string,
    parameters: MonitorChangesParameters
  ): Observable<ApiResponse<MonitorRouteChangesPage>> {
    const url = `/api/monitor/routes/${monitorRouteId}/changes`;
    return this.http.post(url, parameters);
  }

  routeChange(
    monitorRouteId: string,
    changeSetId: string,
    replicationNumber: string
  ): Observable<ApiResponse<MonitorRouteChangePage>> {
    const url = `/api/monitor/routes/${monitorRouteId}/changes/${changeSetId}/${replicationNumber}`;
    return this.http.get(url);
  }

  routeAdd(groupName: string): Observable<ApiResponse<MonitorRouteAddPage>> {
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

  routeInfo(relationId: string): Observable<ApiResponse<MonitorRouteInfoPage>> {
    const url = `/api/monitor/route-info/${relationId}`;
    return this.http.get(url);
  }

  routeGpxUpload(
    groupName: string,
    routeName: string,
    file: File
  ): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}/upload`;
    return this.http.post(url, formData);
  }

  routeAnalyze(
    groupName: string,
    routeName: string
  ): Observable<ApiResponse<void>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}/analyze`;
    return this.http.post(url, '');
  }

  private routeNames(
    groupName: string
  ): Observable<ApiResponse<Array<string>>> {
    const url = `/api/monitor/groups/${groupName}/route-names`;
    return this.http.get(url);
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

  asyncRouteNameUniqueValidator(initialRouteName: string): AsyncValidatorFn {
    return (c: AbstractControl): Observable<ValidationErrors> => {
      if (!c.value || c.value.length === 0 || c.value === initialRouteName) {
        return of(null);
      } else {
        this.store.select(selectMonitorGroupName).pipe(
          mergeMap((groupName) =>
            this.routeNames(groupName).pipe(
              map((response) => response.result),
              map((routeNames) => {
                if (routeNames.includes(c.value)) {
                  return { routeNameNonUnique: c.value };
                }
                return null;
              }),
              catchError(() => of(null))
            )
          )
        );
      }
    };
  }
}
