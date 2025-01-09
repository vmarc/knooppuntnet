import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonToggleChange } from '@angular/material/button-toggle';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatIconModule } from '@angular/material/icon';
import { PlannerPageService } from '../planner-page.service';

@Component({
  selector: 'kpn-route-type-selector',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="planner-route-type-selector">
      <mat-button-toggle-group
        [hideSingleSelectionIndicator]="true"
        [value]="service.routeType()"
        (change)="routeTypeChanged($event)"
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
export class RouteTypeSelectorComponent {
  readonly service = inject(PlannerPageService);

  routeTypeChanged(event: MatButtonToggleChange): void {
    this.service.setRouteType(event.value);
  }
}
