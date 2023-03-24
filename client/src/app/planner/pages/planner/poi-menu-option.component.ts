import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { actionPlannerPoiGroupVisible } from '@app/planner/store/planner-actions';
import { selectPlannerPoisEnabled } from '@app/planner/store/planner-selectors';
import { selectPlannerPoiGroupVisible } from '@app/planner/store/planner-selectors';
import { PoiService } from '@app/services/poi.service';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';

@Component({
  selector: 'kpn-poi-menu-option',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-checkbox
      (click)="$event.stopPropagation()"
      [checked]="visible$ | async"
      [disabled]="!(poisEnabled$ | async)"
      (change)="enabledChanged($event)"
      class="poi-group"
    >
      <ng-content/>
    </mat-checkbox>
  `,
  styles: [
    `
      .poi-group {
        display: block;
        padding-left: 25px;
        padding-right: 10px;
      }
    `,
  ],
})
export class PoiMenuOptionComponent {
  @Input() groupName: string;

  readonly poisEnabled$: Observable<boolean>;
  readonly visible$: Observable<boolean>;

  constructor(private store: Store, public service: PoiService) {
    this.poisEnabled$ = this.store.select(selectPlannerPoisEnabled);
    this.visible$ = this.store.select(
      selectPlannerPoiGroupVisible(this.groupName)
    );
  }

  enabledChanged(event: MatCheckboxChange): void {
    const groupName = this.groupName;
    const visible = event.checked;
    this.store.dispatch(actionPlannerPoiGroupVisible({ groupName, visible }));
  }
}
