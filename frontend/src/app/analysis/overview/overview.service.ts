import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { PageWidthService } from '@app/components/shared';
import { BrowserStorageService } from '@app/services';

@Injectable({
  providedIn: 'root',
})
export class OverviewService {
  private readonly pageWidthService = inject(PageWidthService);
  private readonly browserStorageService = inject(BrowserStorageService);

  readonly list = 'list';
  readonly table = 'table';
  readonly automatic = 'automatic';
  private readonly _formatPreference = signal<string>(this.determineInitialPreference());
  readonly formatPreference = this._formatPreference.asReadonly();
  readonly tableFormat = computed(() => {
    if (this.formatPreference() === this.automatic) {
      return this.pageWidthService.isVeryLarge();
    }
    return this.formatPreference() === this.table;
  });
  private readonly localStorageKey = 'overview-format';

  preferFormat(formatPreference: string): void {
    this._formatPreference.set(formatPreference);
    this.browserStorageService.set(this.localStorageKey, formatPreference);
  }

  private determineInitialPreference(): string {
    let initialFormatPreference = this.browserStorageService.get(this.localStorageKey);
    if (initialFormatPreference === null) {
      initialFormatPreference = this.automatic;
      this.browserStorageService.set(this.localStorageKey, initialFormatPreference);
    }
    return initialFormatPreference;
  }
}
