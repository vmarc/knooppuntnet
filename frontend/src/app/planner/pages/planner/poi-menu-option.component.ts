import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { Signal } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { Store } from '@ngrx/store';
import { actionPlannerPoiGroupVisible } from '../../store/planner-actions';
import { selectPlannerPoisVisible } from '../../store/planner-selectors';
import { selectPlannerPoiGroupVisible } from '../../store/planner-selectors';

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
  imports: [MatCheckboxModule, AsyncPipe],
})
export class PoiMenuOptionComponent implements OnInit {
  groupName = input<string | undefined>();

  private readonly store = inject(Store);
  protected readonly enabled = this.store.selectSignal(selectPlannerPoisVisible);
  protected visible: Signal<boolean>;

  ngOnInit(): void {
    this.visible = this.store.selectSignal(selectPlannerPoiGroupVisible(this.groupName()));
  }

  enabledChanged(event: MatCheckboxChange): void {
    const groupName = this.groupName();
    const visible = event.checked;
    this.store.dispatch(actionPlannerPoiGroupVisible({ groupName, visible }));
  }
}
