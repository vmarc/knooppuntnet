import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { signal } from '@angular/core';
import { PoiDetail } from '@api/common';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { RouterService } from '../../shared/services/router.service';

@Injectable()
export class PoiDetailPageService {
  private readonly apiService = inject(ApiService);
  private readonly routerService = inject(RouterService);

  private readonly _response = signal<ApiResponse<PoiDetail>>(null);
  readonly response = this._response.asReadonly();

  onInit(): void {
    const elementType = this.routerService.param('elementType');
    const elementId = +this.routerService.param('elementId');
    this.load(elementType, elementId);
  }

  private load(elementType: string, elementId: number): void {
    this.apiService.poiDetail(elementType, elementId).subscribe((response) => {
      this._response.set(response);
    });
  }
}
