import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { ApiResponse } from '@api/custom';
import { ApiService } from '@app/services';
import { PoiMapService } from './components/poi-map.service';

@Injectable()
export class PoiAreasPageService {
  private readonly apiService = inject(ApiService);
  private readonly poiMapService = inject(PoiMapService);

  private readonly _response = signal<ApiResponse<string>>(null);
  readonly response = this._response.asReadonly();

  onInit(): void {
    this.apiService.poiAreas().subscribe((response) => {
      this._response.set(response);
    });
  }

  afterViewInit(): void {
    this.poiMapService.init(this.response().result);
  }
}
