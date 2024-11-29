import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { signal } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { State } from '@app/state';

@Component({
  selector: 'kpn-explore-poi-option',
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
export class ExplorePoiOptionComponent {
  private readonly state = inject(State);

  groupName = input.required<string>();

  protected readonly enabled = signal<boolean>(true);
  protected visible = computed(() => this.state.map.poiActive().get(this.groupName()));

  enabledChanged(event: MatCheckboxChange): void {
    this.state.map.updatePoiGroupActive(this.groupName(), event.checked);
  }
}
