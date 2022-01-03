import { Input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatRadioChange } from '@angular/material/radio';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { AppState } from '../../core/core.state';
import { actionPreferencesAnalysisStrategy } from '../../core/preferences/preferences.actions';
import { selectPreferencesAnalysisStrategy } from '../../core/preferences/preferences.selectors';

@Component({
  selector: 'kpn-analysis-strategy',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="sidebar-section">
      <div class="sidebar-section-title">Analysis strategy</div>

      <mat-radio-group
        [value]="strategy$ | async"
        (change)="modeChanged($event)"
      >
        <mat-radio-button
          value="location"
          title="Location"
          i18n="@@analysis.by-location"
        >
          Explore by location
        </mat-radio-button>
        <mat-radio-button
          value="network"
          title="Network"
          i18n="@@analysis.by-network"
        >
          Explore by network
        </mat-radio-button>
      </mat-radio-group>
    </div>
  `,
  styleUrls: ['../../components/shared/sidebar/sidebar.scss'],
})
export class AnalysisStrategyComponent {
  @Input() url: string;
  readonly strategy$ = this.store.select(selectPreferencesAnalysisStrategy);

  constructor(private router: Router, private store: Store<AppState>) {}

  modeChanged(event: MatRadioChange) {
    this.store.dispatch(
      actionPreferencesAnalysisStrategy({ strategy: event.value })
    );
    if (this.url) {
      this.router.navigateByUrl(this.url);
    }
  }
}
