import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorRouteInfoPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorRoutePropertiesStep3RelationService {
  private readonly _apiResponse =
    signal<ApiResponse<MonitorRouteInfoPage> | null>(null);
  readonly apiResponse = this._apiResponse.asReadonly();

  constructor(private monitorService: MonitorService) {}

  getRouteInformation(relationId: number): void {
    this.monitorService.routeInfo(relationId).subscribe((response) => {
      this._apiResponse.set(response);
    });
  }

  resetRouteInformation(): void {
    this._apiResponse.set(null);
  }
}
