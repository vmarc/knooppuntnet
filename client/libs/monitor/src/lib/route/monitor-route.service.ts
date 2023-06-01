import { Injectable } from '@angular/core';
import { MonitorRouteSaveResult } from '@api/common/monitor';
import { MonitorRouteProperties } from '@api/common/monitor';
import { Day } from '@api/custom';
import { ApiResponse } from '@api/custom';
import { Observable } from 'rxjs';
import { MonitorService } from '../monitor.service';

@Injectable()
export class MonitorRouteService {
  constructor(private monitorService: MonitorService) {}

  routeAdd(
    groupName: string,
    properties: MonitorRouteProperties
  ): Observable<ApiResponse<MonitorRouteSaveResult>> {
    return this.monitorService.routeAdd(groupName, properties);
  }

  routeGpxUpload(
    groupName: string,
    name: string,
    referenceFile: File
  ): Observable<void> {
    return this.monitorService.routeGpxUpload(groupName, name, referenceFile);
  }

  routeSubRelationGpxUpload(
    groupName: string,
    name: string,
    relationId: string,
    referenceFile: File,
    referenceDay: Day
  ): Observable<void> {
    return this.monitorService.routeSubRelationGpxUpload(
      groupName,
      name,
      relationId,
      referenceFile,
      referenceDay
    );
  }

  routeAnalyze(
    groupName: string,
    name: string
  ): Observable<ApiResponse<MonitorRouteSaveResult>> {
    return this.monitorService.routeAnalyze(groupName, name);
  }

  routeUpdate(
    groupName: string,
    name: string,
    properties: MonitorRouteProperties
  ): Observable<ApiResponse<MonitorRouteSaveResult>> {
    return this.monitorService.routeUpdate(groupName, name, properties);
  }
}