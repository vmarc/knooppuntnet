import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Subset } from '@api/custom/subset';
import { Store } from '@ngrx/store';
import { map } from 'rxjs/operators';
import { AppState } from '../../core/core.state';
import { WindowService } from '../../services/window.service';
import { selectSubset } from './store/subset.selectors';

@Component({
  selector: 'kpn-subset-analysis-mode',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: ` <kpn-analysis-mode [url]="url$ | async"></kpn-analysis-mode> `,
})
export class SubsetAnalysisModeComponent {
  readonly url$ = this.store
    .select(selectSubset)
    .pipe(map((subset: Subset) => this.url(subset)));

  constructor(
    private store: Store<AppState>,
    private windowService: WindowService
  ) {}

  private url(subset: Subset): string {
    let url = `/analysis/${subset.networkType}/${subset.country}`;
    const language = this.windowService.language();
    if (language.length > 0) {
      url = `/${language}${url}`;
    }
    return url;
  }
}
