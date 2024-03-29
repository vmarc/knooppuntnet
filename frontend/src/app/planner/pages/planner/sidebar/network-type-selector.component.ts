import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonToggleChange } from '@angular/material/button-toggle';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatIconModule } from '@angular/material/icon';
import { PlannerPageService } from '../planner-page.service';

@Component({
  selector: 'kpn-network-type-selector',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-button-toggle-group [value]="service.networkType()" (change)="networkTypeChanged($event)">
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
  styles: `
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
  standalone: true,
  imports: [MatButtonToggleModule, MatIconModule, AsyncPipe],
})
export class NetworkTypeSelectorComponent {
  protected readonly service = inject(PlannerPageService);

  networkTypeChanged(event: MatButtonToggleChange): void {
    this.service.setNetworkType(event.value);
  }
}
