import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonToggleChange } from '@angular/material/button-toggle';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { NzIconDirective } from 'ng-zorro-antd/icon';
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
          <nz-icon nzType="cycling" />
        </mat-button-toggle>
        <mat-button-toggle value="hiking">
          <nz-icon nzType="hiking" />
        </mat-button-toggle>
        <mat-button-toggle value="horse-riding">
          <nz-icon nzType="horse-riding" />
        </mat-button-toggle>
        <mat-button-toggle value="motorboat">
          <nz-icon nzType="motorboat" />
        </mat-button-toggle>
        <mat-button-toggle value="canoe">
          <nz-icon nzType="canoe" />
        </mat-button-toggle>
        <mat-button-toggle value="inline-skating">
          <nz-icon nzType="inline-skating" />
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
  imports: [MatButtonToggleModule, NzIconDirective],
})
export class RouteTypeSelectorComponent {
  readonly service = inject(PlannerPageService);

  routeTypeChanged(event: MatButtonToggleChange): void {
    this.service.setRouteType(event.value);
  }
}
