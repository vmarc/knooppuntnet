import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { combineLatest } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { map } from 'rxjs/operators';
import { PageWidth } from '@app/components/shared/page-width';
import { PageWidthService } from '@app/components/shared/page-width.service';
import { BrowserStorageService } from '@app/services/browser-storage.service';

@Injectable({
  providedIn: 'root',
})
export class OverviewService {
  readonly list = 'list';
  readonly table = 'table';
  readonly automatic = 'automatic';
  readonly tableFormat$: Observable<boolean>;
  readonly formatPreference$: BehaviorSubject<string>;
  private readonly localStorageKey = 'overview-format';

  constructor(
    private pageWidthService: PageWidthService,
    private browserStorageService: BrowserStorageService
  ) {
    this.formatPreference$ = new BehaviorSubject(
      this.determineInitialPreference()
    );
    this.tableFormat$ = combineLatest([
      this.formatPreference$,
      this.pageWidthService.current$,
    ]).pipe(
      map(([formatPreference, pageWidth]) => {
        if (formatPreference === this.automatic) {
          return pageWidth === PageWidth.veryLarge;
        }
        return formatPreference === this.table;
      })
    );
  }

  preferFormat(formatPreference: string): void {
    this.formatPreference$.next(formatPreference);
    this.browserStorageService.set(this.localStorageKey, formatPreference);
  }

  private determineInitialPreference(): string {
    let initialFormatPreference = this.browserStorageService.get(
      this.localStorageKey
    );
    if (initialFormatPreference === null) {
      initialFormatPreference = this.automatic;
      this.browserStorageService.set(
        this.localStorageKey,
        initialFormatPreference
      );
    }
    return initialFormatPreference;
  }
}
