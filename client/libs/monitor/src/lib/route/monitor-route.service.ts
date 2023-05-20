import { MonitorRouteSaveResult } from '@api/common/monitor';
import { MonitorRouteProperties } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';
import { Observable } from 'rxjs';

export interface MonitorRouteService {
  routeAdd(
    groupName: string,
    properties: MonitorRouteProperties
  ): Observable<ApiResponse<MonitorRouteSaveResult>>;

  routeGpxUpload(
    groupName: string,
    name: string,
    referenceFile: File,
    relationId: number
  ): Observable<void>;

  routeAnalyze(groupName: string, name: string): Observable<ApiResponse<void>>;

  routeUpdate(
    groupName: string,
    name: string,
    properties: MonitorRouteProperties
  ): Observable<ApiResponse<MonitorRouteSaveResult>>;
}
