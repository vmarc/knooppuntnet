import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatExpansionModule } from '@angular/material/expansion';
import { actionPreferencesShowOptions } from '@app/core';
import { actionPreferencesPlanProposed } from '@app/core';
import { actionPreferencesShowProposed } from '@app/core';
import { selectPreferencesShowOptions } from '@app/core';
import { selectPreferencesPlanProposed } from '@app/core';
import { selectPreferencesShowProposed } from '@app/core';
import { Store } from '@ngrx/store';

@Component({
  selector: 'kpn-planner-sidebar-options',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-expansion-panel [expanded]="expanded$ | async" (expandedChange)="expandedChanged($event)">
      <mat-expansion-panel-header i18n="@@planner.options"> Options </mat-expansion-panel-header>
      <p>
        <mat-checkbox [checked]="showProposed$ | async" (change)="showProposedChanged($event)">
          <span i18n="@@planner.options.show-proposed">Show proposed routes</span>
        </mat-checkbox>
        <mat-checkbox [checked]="planProposed$ | async" (change)="planProposedChanged($event)">
          <span i18n="@@planner.options.plan-proposed">Allow planning proposed routes</span>
        </mat-checkbox>
      </p>
    </mat-expansion-panel>
  `,
  styles: `
    .legend > div {
      display: flex;
      align-items: center;
    }
  `,
  standalone: true,
  imports: [MatExpansionModule, MatCheckboxModule, AsyncPipe],
})
export class PlannerSideBarOptionsComponent {
  readonly expanded$ = this.store.select(selectPreferencesShowOptions);
  readonly showProposed$ = this.store.select(selectPreferencesShowProposed);
  readonly planProposed$ = this.store.select(selectPreferencesPlanProposed);

  constructor(private store: Store) {}

  expandedChanged(expanded: boolean): void {
    this.store.dispatch(actionPreferencesShowOptions({ value: expanded }));
  }

  showProposedChanged(event: MatCheckboxChange) {
    this.store.dispatch(actionPreferencesShowProposed({ value: event.checked }));
  }

  planProposedChanged(event: MatCheckboxChange) {
    this.store.dispatch(actionPreferencesPlanProposed({ value: event.checked }));
  }
}
