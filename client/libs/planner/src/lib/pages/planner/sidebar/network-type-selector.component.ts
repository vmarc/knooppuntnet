import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonToggleChange } from '@angular/material/button-toggle';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatIconModule } from '@angular/material/icon';
import { NetworkType } from '@api/custom';
import { Store } from '@ngrx/store';
import { actionPlannerNetworkType } from '../../../store/planner-actions';
import { selectPlannerNetworkType } from '../../../store/planner-selectors';

@Component({
  selector: 'kpn-network-type-selector',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-button-toggle-group
      [value]="networkType()"
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
  standalone: true,
  imports: [MatButtonToggleModule, MatIconModule, AsyncPipe],
})
export class NetworkTypeSelectorComponent {
  readonly networkType = this.store.selectSignal(selectPlannerNetworkType);

  constructor(private store: Store) {}

  networkTypeChanged(event: MatButtonToggleChange) {
    const networkType: NetworkType = event.value;
    this.store.dispatch(actionPlannerNetworkType({ networkType }));
  }
}
