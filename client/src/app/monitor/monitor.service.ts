import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MonitorChangesPage } from '@api/common/monitor/monitor-changes-page';
import { MonitorChangesParameters } from '@api/common/monitor/monitor-changes-parameters';
import { MonitorGroupChangesPage } from '@api/common/monitor/monitor-group-changes-page';
import { MonitorGroupPage } from '@api/common/monitor/monitor-group-page';
import { MonitorGroupProperties } from '@api/common/monitor/monitor-group-properties';
import { MonitorGroupsPage } from '@api/common/monitor/monitor-groups-page';
import { MonitorRouteAdd } from '@api/common/monitor/monitor-route-add';
import { MonitorRouteChangePage } from '@api/common/monitor/monitor-route-change-page';
import { MonitorRouteChangesPage } from '@api/common/monitor/monitor-route-changes-page';
import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';
import { MonitorRouteInfoPage } from '@api/common/monitor/monitor-route-info-page';
import { MonitorRouteMapPage } from '@api/common/monitor/monitor-route-map-page';
import { ApiResponse } from '@api/custom/api-response';
import { Observable } from 'rxjs';

@Injectable()
export class MonitorService {
  constructor(private http: HttpClient) {}

  public groups(): Observable<ApiResponse<MonitorGroupsPage>> {
    const url = '/api/monitor/groups';
    return this.http.get(url);
  }

  public group(groupName: string): Observable<ApiResponse<MonitorGroupPage>> {
    const url = `/api/monitor/groups/${groupName}`;
    return this.http.get(url);
  }

  public addGroup(properties: MonitorGroupProperties): Observable<any> {
    const url = `/api/monitor/groups`;
    return this.http.post(url, properties);
  }

  public updateGroup(
    groupId: string,
    properties: MonitorGroupProperties
  ): Observable<any> {
    const url = `/api/monitor/groups/${groupId}`;
    return this.http.put(url, properties);
  }

  public deleteGroup(groupId: string): Observable<any> {
    const url = `/api/monitor/groups/${groupId}`;
    return this.http.delete(url);
  }

  public groupChanges(
    groupName: string,
    parameters: MonitorChangesParameters
  ): Observable<ApiResponse<MonitorGroupChangesPage>> {
    const url = `/api/monitor/groups/${groupName}/changes`;
    return this.http.post(url, parameters);
  }

  public changes(
    parameters: MonitorChangesParameters
  ): Observable<ApiResponse<MonitorChangesPage>> {
    const url = `/api/monitor/changes`;
    return this.http.post(url, parameters);
  }

  public addRoute(
    groupName: string,
    add: MonitorRouteAdd
  ): Observable<ApiResponse<MonitorGroupPage>> {
    const url = `/api/monitor/groups/${groupName}`;
    return this.http.post(url, add);
  }

  public route(
    groupName: string,
    routeName: string
  ): Observable<ApiResponse<MonitorRouteDetailsPage>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}`;
    return this.http.get(url);
  }

  public deleteRoute(
    groupName: string,
    routeName: string
  ): Observable<ApiResponse<void>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}`;
    return this.http.delete(url);
  }

  public routeMap(
    groupName: string,
    routeName: string
  ): Observable<ApiResponse<MonitorRouteMapPage>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}/map`;
    return this.http.get(url);
  }

  public routeChanges(
    monitorRouteId: string,
    parameters: MonitorChangesParameters
  ): Observable<ApiResponse<MonitorRouteChangesPage>> {
    const url = `/api/monitor/routes/${monitorRouteId}/changes`;
    return this.http.post(url, parameters);
  }

  public routeChange(
    monitorRouteId: string,
    changeSetId: string,
    replicationNumber: string
  ): Observable<ApiResponse<MonitorRouteChangePage>> {
    const url = `/api/monitor/routes/${monitorRouteId}/changes/${changeSetId}/${replicationNumber}`;
    return this.http.get(url);
  }

  public routeInfo(
    routeId: number
  ): Observable<ApiResponse<MonitorRouteInfoPage>> {
    const url = `/api/monitor/route-info/${routeId}`;
    return this.http.get(url);
  }

  public routeGpxUpload(
    groupName: string,
    routeName: string,
    file: File
  ): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}/upload`;
    return this.http.post(url, formData);
  }
}
