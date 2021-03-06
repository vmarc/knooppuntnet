import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { PlannerService } from '../planner.service';
import { Store } from '@ngrx/store';
import { select } from '@ngrx/store';
import { AppState } from '../../core/core.state';
import { selectPreferencesInstructions } from '../../core/preferences/preferences.selectors';

@Component({
  selector: 'kpn-plan-result-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="menu" *ngIf="mode$ | async as mode">
      <span>
        <a
          [ngClass]="{ selected: mode === 'compact' }"
          (click)="compact($event)"
          i18n="@@planner.compact"
        >
          Compact
        </a>
      </span>
      <span>
        <a
          [ngClass]="{ selected: mode === 'detailed' }"
          (click)="detailed($event)"
          i18n="@@planner.detailed"
        >
          Detailed
        </a>
      </span>
      <span *ngIf="instructions$ | async">
        <a
          [ngClass]="{ selected: mode === 'instructions' }"
          (click)="instructions($event)"
          i18n="@@planner.instructions"
        >
          Instructions
        </a>
      </span>
    </div>
  `,
  styles: [
    `
      .menu {
        padding-bottom: 5px;
      }

      .menu :not(:last-child):after {
        content: ' | ';
        padding-left: 5px;
        padding-right: 5px;
      }

      a.selected {
        color: rgba(0, 0, 0, 0.87);
        font-weight: bold;
      }

      a:hover {
        cursor: pointer;
      }
    `,
  ],
})
export class PlanResultMenuComponent implements OnInit {
  mode$: BehaviorSubject<string>;
  readonly instructions$ = this.store.pipe(
    select(selectPreferencesInstructions)
  );

  constructor(
    private plannerService: PlannerService,
    private store: Store<AppState>
  ) {}

  ngOnInit(): void {
    this.mode$ = this.plannerService.resultMode$;
  }

  compact(event) {
    this.mode$.next('compact');
    event.stopPropagation();
  }

  detailed(event) {
    this.mode$.next('detailed');
    event.stopPropagation();
  }

  instructions(event) {
    this.mode$.next('instructions');
    event.stopPropagation();
  }
}
