import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { PoiService } from '@app/services';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { actionPlannerPoiGroupVisible } from '../../store/planner-actions';
import { selectPlannerPoisEnabled } from '../../store/planner-selectors';
import { selectPlannerPoiGroupVisible } from '../../store/planner-selectors';

@Component({
  selector: 'kpn-poi-menu-option',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-checkbox
      (click)="$event.stopPropagation()"
      [checked]="visible$ | async"
      [disabled]="(poisEnabled$ | async) === false"
      (change)="enabledChanged($event)"
      class="poi-group"
    >
      <ng-content />
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
  standalone: true,
  imports: [MatCheckboxModule, AsyncPipe],
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
