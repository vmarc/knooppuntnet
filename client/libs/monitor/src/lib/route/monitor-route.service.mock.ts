import { MonitorRouteSaveResult } from '@api/common/monitor';
import { MonitorRouteProperties } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';
import { Observable } from 'rxjs';
import { MonitorRouteService } from './monitor-route.service';

/* eslint-disable @typescript-eslint/no-unused-vars */
export class MonitorRouteServiceMock implements MonitorRouteService {
  routeAdd(
    groupName: string,
    properties: MonitorRouteProperties
  ): Observable<ApiResponse<MonitorRouteSaveResult>> {
    throw new Error('unexpected call');
  }

  routeAnalyze(groupName: string, name: string): Observable<ApiResponse<void>> {
    throw new Error('unexpected call');
  }

  routeGpxUpload(
    groupName: string,
    name: string,
    referenceFile: File,
    relationId: number
  ): Observable<void> {
    throw new Error('unexpected call');
  }

  routeUpdate(
    groupName: string,
    name: string,
    properties: MonitorRouteProperties
  ): Observable<ApiResponse<MonitorRouteSaveResult>> {
    throw new Error('unexpected call');
  }
}
