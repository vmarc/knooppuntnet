import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { PoiService } from '@app/services';
import { PlannerStateService } from '../planner-state.service';

@Component({
  selector: 'kpn-poi-menu-option',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-checkbox
      (click)="$event.stopPropagation()"
      [checked]="visible()"
      [disabled]="enabled() === false"
      (change)="enabledChanged($event)"
      class="poi-group"
    >
      <ng-content />
    </mat-checkbox>
  `,
  styles: `
    .poi-group {
      display: block;
      padding-left: 25px;
      padding-right: 10px;
    }
  `,
  standalone: true,
  imports: [MatCheckboxModule],
})
export class PoiMenuOptionComponent {
  groupName = input.required<string>();

  private readonly plannerStateService = inject(PlannerStateService);
  private readonly poiService = inject(PoiService);
  protected readonly enabled = this.plannerStateService.poisVisible;
  protected visible = computed(() => this.plannerStateService.poiGroupVisible(this.groupName()));

  enabledChanged(event: MatCheckboxChange): void {
    this.plannerStateService.setPoiGroupVisible(this.groupName(), event.checked);
    this.poiService.updateGroupEnabled(this.groupName(), event.checked);
  }
}
