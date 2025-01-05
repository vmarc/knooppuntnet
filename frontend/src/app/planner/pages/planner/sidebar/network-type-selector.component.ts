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
    <div class="planner-network-type-selector">
      <mat-button-toggle-group
        [hideSingleSelectionIndicator]="true"
        [value]="service.networkType()"
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
    </div>
  `,
  styles: `
    mat-button-toggle {
      width: 34px;
      height: 34px;
    }
  `,
  imports: [MatButtonToggleModule, MatIconModule],
})
export class NetworkTypeSelectorComponent {
  readonly service = inject(PlannerPageService);

  networkTypeChanged(event: MatButtonToggleChange): void {
    this.service.setNetworkType(event.value);
  }
}
