import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { Store } from '@ngrx/store';
import { AppState } from '../../core/core.state';
import { actionPreferencesShowOptions } from '../../core/preferences/preferences.actions';
import { actionPreferencesPlanProposed } from '../../core/preferences/preferences.actions';
import { actionPreferencesShowProposed } from '../../core/preferences/preferences.actions';
import { selectPreferencesShowOptions } from '../../core/preferences/preferences.selectors';
import { selectPreferencesPlanProposed } from '../../core/preferences/preferences.selectors';
import { selectPreferencesShowProposed } from '../../core/preferences/preferences.selectors';

@Component({
  selector: 'kpn-map-sidebar-options',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-expansion-panel
      [expanded]="expanded$ | async"
      (expandedChange)="expandedChanged($event)"
    >
      <mat-expansion-panel-header i18n="@@planner.options">
        Options
      </mat-expansion-panel-header>
      <p>
        <mat-checkbox
          [checked]="showProposed$ | async"
          (change)="showProposedChanged($event)"
        >
          <span i18n="@@planner.options.show-proposed"
            >Show proposed routes</span
          >
        </mat-checkbox>
        <mat-checkbox
          [checked]="planProposed$ | async"
          (change)="planProposedChanged($event)"
        >
          <span i18n="@@planner.options.plan-proposed"
            >Allow planning proposed routes</span
          >
        </mat-checkbox>
      </p>
    </mat-expansion-panel>
  `,
  styles: [
    `
      .legend > div {
        display: flex;
        align-items: center;
      }

      .title {
        padding-bottom: 15px;
      }

      .legend-icon {
        width: 60px;
        padding-right: 10px;
        text-align: center;
      }
    `,
  ],
})
export class MapSidebarOptionsComponent {
  readonly expanded$ = this.store.select(selectPreferencesShowOptions);
  readonly showProposed$ = this.store.select(selectPreferencesShowProposed);
  readonly planProposed$ = this.store.select(selectPreferencesPlanProposed);

  constructor(private store: Store<AppState>) {}

  expandedChanged(expanded: boolean): void {
    this.store.dispatch(actionPreferencesShowOptions({ value: expanded }));
  }

  showProposedChanged(event: MatCheckboxChange) {
    this.store.dispatch(
      actionPreferencesShowProposed({ value: event.checked })
    );
  }

  planProposedChanged(event: MatCheckboxChange) {
    this.store.dispatch(
      actionPreferencesPlanProposed({ value: event.checked })
    );
  }
}
