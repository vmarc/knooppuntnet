import { ChangeDetectionStrategy, Component } from '@angular/core';
import { MatButtonToggleChange } from '@angular/material/button-toggle';
import { NetworkType } from '@api/custom/network-type';
import { actionPlannerNetworkType } from '@app/planner/store/planner-actions';
import { selectPlannerNetworkType } from '@app/planner/store/planner-selectors';
import { Store } from '@ngrx/store';

@Component({
  selector: 'kpn-network-type-selector',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-button-toggle-group
      [value]="networkType$ | async"
      (change)="networkTypeChanged($event)"
    >
      <mat-button-toggle value="cycling">
        <mat-icon svgIcon="cycling" />
      </mat-button-toggle>
      <mat-button-toggle value="hiking">
        <mat-icon svgIcon="hiking" />
      </mat-button-toggle>
      <mat-button-toggle value="horse-riding">
        <mat-icon svgIcon="horse-riding" />
      </mat-button-toggle>
      <mat-button-toggle value="motorboat">
        <mat-icon svgIcon="motorboat" />
      </mat-button-toggle>
      <mat-button-toggle value="canoe">
        <mat-icon svgIcon="canoe" />
      </mat-button-toggle>
      <mat-button-toggle value="inline-skating">
        <mat-icon svgIcon="inline-skating" />
      </mat-button-toggle>
    </mat-button-toggle-group>
  `,
  styles: [
    `
      :host ::ng-deep .mat-button-toggle > .mat-button-toggle-button {
        width: 34px;
        height: 34px;
      }

      :host
        ::ng-deep
        .mat-button-toggle
        > .mat-button-toggle-button
        > .mat-button-toggle-label-content {
        line-height: 34px;
        color: rgba(0, 0, 0, 0.8);
        padding: 0;
      }
    `,
  ],
})
export class NetworkTypeSelectorComponent {
  readonly networkType$ = this.store.select(selectPlannerNetworkType);

  constructor(private store: Store) {}

  networkTypeChanged(event: MatButtonToggleChange) {
    const networkType: NetworkType = event.value;
    this.store.dispatch(actionPlannerNetworkType({ networkType }));
  }
}
