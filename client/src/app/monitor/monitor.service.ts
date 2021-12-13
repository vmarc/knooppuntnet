import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MonitorAdminGroupPage } from '@api/common/monitor/monitor-admin-group-page';
import { MonitorChangesPage } from '@api/common/monitor/monitor-changes-page';
import { MonitorChangesParameters } from '@api/common/monitor/monitor-changes-parameters';
import { MonitorGroup } from '@api/common/monitor/monitor-group';
import { MonitorGroupChangesPage } from '@api/common/monitor/monitor-group-changes-page';
import { MonitorGroupPage } from '@api/common/monitor/monitor-group-page';
import { MonitorGroupsPage } from '@api/common/monitor/monitor-groups-page';
import { MonitorRouteChangePage } from '@api/common/monitor/monitor-route-change-page';
import { MonitorRouteChangesPage } from '@api/common/monitor/monitor-route-changes-page';
import { MonitorRouteDetailsPage } from '@api/common/monitor/monitor-route-details-page';
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

  public monitorRoute(
    routeId: string
  ): Observable<ApiResponse<MonitorRouteDetailsPage>> {
    const url = `/api/monitor/routes/${routeId}`;
    return this.http.get(url);
  }

  public monitorRouteMap(
    routeId: string
  ): Observable<ApiResponse<MonitorRouteMapPage>> {
    const url = `/api/monitor/routes/${routeId}/map`;
    return this.http.get(url);
  }

  public monitorRouteChanges(
    routeId: string,
    parameters: MonitorChangesParameters
  ): Observable<ApiResponse<MonitorRouteChangesPage>> {
    const url = `/api/monitor/routes/${routeId}/changes`;
    return this.http.post(url, parameters);
  }

  public monitorRouteChange(
    routeId: string,
    changeSetId: string,
    replicationNumber: string
  ): Observable<ApiResponse<MonitorRouteChangePage>> {
    const url = `/api/monitor/routes/${routeId}/changes/${changeSetId}/${replicationNumber}`;
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
}
