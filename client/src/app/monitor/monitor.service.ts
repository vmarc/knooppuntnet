import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MonitorAdminGroupPage } from '@api/common/monitor/monitor-admin-group-page';
import { MonitorChangesPage } from '@api/common/monitor/monitor-changes-page';
import { MonitorChangesParameters } from '@api/common/monitor/monitor-changes-parameters';
import { MonitorGroup } from '@api/common/monitor/monitor-group';
import { MonitorGroupChangesPage } from '@api/common/monitor/monitor-group-changes-page';
import { MonitorGroupPage } from '@api/common/monitor/monitor-group-page';
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

  public monitorGroups(): Observable<ApiResponse<MonitorGroupsPage>> {
    const url = '/api/monitor/groups';
    return this.http.get(url);
  }

  public monitorGroup(
    groupName: string
  ): Observable<ApiResponse<MonitorGroupPage>> {
    const url = `/api/monitor/groups/${groupName}`;
    return this.http.get(url);
  }

  public monitorGroupChanges(
    groupName: string,
    parameters: MonitorChangesParameters
  ): Observable<ApiResponse<MonitorGroupChangesPage>> {
    const url = `/api/monitor/groups/${groupName}/changes`;
    return this.http.post(url, parameters);
  }

  public monitorChanges(
    parameters: MonitorChangesParameters
  ): Observable<ApiResponse<MonitorChangesPage>> {
    const url = `/api/monitor/changes`;
    return this.http.post(url, parameters);
  }

  public monitorRoutes(): Observable<ApiResponse<MonitorGroupPage>> {
    const url = '/api/monitor/routes';
    return this.http.get(url);
  }

  public monitorRouteInfo(
    routeId: number
  ): Observable<ApiResponse<MonitorRouteInfoPage>> {
    const url = `/admin-api/monitor/route-info/${routeId}`;
    return this.http.get(url);
  }

  public addMonitorRoute(
    groupName: string,
    add: MonitorRouteAdd
  ): Observable<ApiResponse<MonitorGroupPage>> {
    const url = `/admin-api/monitor/groups/${groupName}`;
    return this.http.post(url, add);
  }

  public monitorRoute(
    groupName: string,
    routeName: string
  ): Observable<ApiResponse<MonitorRouteDetailsPage>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}`;
    return this.http.get(url);
  }

  public monitorRouteDelete(
    groupName: string,
    routeName: string
  ): Observable<ApiResponse<void>> {
    const url = `/admin-api/monitor/groups/${groupName}/routes/${routeName}`;
    return this.http.delete(url);
  }

  public monitorRouteMap(
    groupName: string,
    routeName: string
  ): Observable<ApiResponse<MonitorRouteMapPage>> {
    const url = `/api/monitor/groups/${groupName}/routes/${routeName}/map`;
    return this.http.get(url);
  }

  public monitorRouteChanges(
    monitorRouteId: string,
    parameters: MonitorChangesParameters
  ): Observable<ApiResponse<MonitorRouteChangesPage>> {
    const url = `/api/monitor/routes/${monitorRouteId}/changes`;
    return this.http.post(url, parameters);
  }

  public monitorRouteChange(
    monitorRouteId: string,
    changeSetId: string,
    replicationNumber: string
  ): Observable<ApiResponse<MonitorRouteChangePage>> {
    const url = `/api/monitor/routes/${monitorRouteId}/changes/${changeSetId}/${replicationNumber}`;
    return this.http.get(url);
  }

  public monitorAdminGroups(): Observable<ApiResponse<MonitorGroupsPage>> {
    const url = '/admin-api/monitor/groups';
    return this.http.get(url);
  }

  public monitorAdminRouteGroup(
    groupName: string
  ): Observable<ApiResponse<MonitorAdminGroupPage>> {
    const url = '/admin-api/monitor/groups/' + groupName;
    return this.http.get(url);
  }

  public monitorAdminAddRouteGroup(group: MonitorGroup): Observable<any> {
    const url = `/admin-api/monitor/groups`;
    return this.http.post(url, group);
  }

  public monitorAdminDeleteRouteGroup(groupName: string): Observable<any> {
    const url = `/admin-api/monitor/groups/${groupName}`;
    return this.http.delete(url);
  }

  public monitorAdminUpdateRouteGroup(group: MonitorGroup): Observable<any> {
    const url = `/admin-api/monitor/groups/${group._id}`;
    return this.http.put(url, group);
  }

  public routeGpxUpload(
    groupName: string,
    routeName: string,
    file: File
  ): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    const url = `/admin-api/monitor/groups/${groupName}/routes/${routeName}/upload`;
    return this.http.post(url, formData);
  }
}
