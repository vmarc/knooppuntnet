import { MonitorRouteSaveResult } from '@api/common/monitor';
import { MonitorRouteProperties } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';
import { Observable } from 'rxjs';
import { MonitorRouteService } from './monitor-route.service';

/* eslint-disable @typescript-eslint/no-unused-vars */
export class MonitorRouteServiceMock extends MonitorRouteService {
  constructor() {
    super(null);
  }
 
  override routeAdd(
    groupName: string,
    properties: MonitorRouteProperties
  ): Observable<ApiResponse<MonitorRouteSaveResult>> {
    throw new Error('unexpected call');
  }

  override routeAnalyze(
    groupName: string,
    name: string
  ): Observable<ApiResponse<void>> {
    throw new Error('unexpected call');
  }

  override routeGpxUpload(
    groupName: string,
    name: string,
    referenceFile: File,
    relationId: number
  ): Observable<void> {
    throw new Error('unexpected call');
  }

  override routeUpdate(
    groupName: string,
    name: string,
    properties: MonitorRouteProperties
  ): Observable<ApiResponse<MonitorRouteSaveResult>> {
    throw new Error('unexpected call');
  }
}
