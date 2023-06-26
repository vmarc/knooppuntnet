import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { Injectable } from '@angular/core';
import { MonitorRouteInfoPage } from '@api/common/monitor';
import { ApiResponse } from '@api/custom';
import { MonitorService } from '../../monitor.service';

@Injectable()
export class MonitorRoutePropertiesStep3RelationService {
  readonly #monitorService = inject(MonitorService);
  readonly #apiResponse = signal<ApiResponse<MonitorRouteInfoPage> | null>(
    null
  );
  readonly apiResponse = this.#apiResponse.asReadonly();

  getRouteInformation(relationId: number): void {
    this.#monitorService.routeInfo(relationId).subscribe((response) => {
      this.#apiResponse.set(response);
    });
  }

  resetRouteInformation(): void {
    this.#apiResponse.set(null);
  }
}
