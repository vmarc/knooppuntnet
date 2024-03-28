import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { StatisticValues } from '@api/common/statistics';
import { ApiResponse } from '@api/custom';
import { PageWidthService } from '@app/components/shared';
import { ApiService } from '@app/services';
import { BrowserStorageService } from '@app/services';
import { OverviewFormat } from './components/overview-format';

@Injectable({
  providedIn: 'root',
})
export class OverviewPageService {
  private readonly apiService = inject(ApiService);
  private readonly pageWidthService = inject(PageWidthService);
  private readonly browserStorageService = inject(BrowserStorageService);
  private readonly _response = signal<ApiResponse<StatisticValues[]>>(null);
  readonly response = this._response.asReadonly();

  private readonly _preferredFormat = signal<OverviewFormat>(this.initialPreferredFormat());
  readonly preferredFormat = this._preferredFormat.asReadonly();
  readonly tableFormat = computed(() => {
    if (this.preferredFormat() === 'automatic') {
      return this.pageWidthService.isVeryLarge();
    }
    return this.preferredFormat() === 'table';
  });
  private readonly localStorageKey = 'overview-format';

  onInit(): void {
    this.apiService.overview().subscribe((response) => this._response.set(response));
  }

  preferFormat(format: OverviewFormat): void {
    this._preferredFormat.set(format);
    this.browserStorageService.set(this.localStorageKey, format);
  }

  private initialPreferredFormat(): OverviewFormat {
    let format: OverviewFormat = 'automatic';
    const formatString = this.browserStorageService.get(this.localStorageKey);
    if (formatString === 'list') {
      format = formatString;
    } else if (formatString === 'table') {
      format = formatString;
    } else if (formatString === 'automatic') {
      format = formatString;
    } else {
      this.browserStorageService.set(this.localStorageKey, format);
    }
    return format;
  }
}
