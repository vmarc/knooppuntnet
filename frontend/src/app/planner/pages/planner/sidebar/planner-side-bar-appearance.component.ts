import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatRadioChange } from '@angular/material/radio';
import { MatRadioModule } from '@angular/material/radio';
import { actionPreferencesShowAppearanceOptions } from '@app/core';
import { selectPreferencesShowAppearanceOptions } from '@app/core';
import { Store } from '@ngrx/store';
import { actionPlannerMapMode } from '../../../store/planner-actions';
import { selectPlannerMapMode } from '../../../store/planner-selectors';

@Component({
  selector: 'kpn-planner-sidebar-appearance',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-expansion-panel [expanded]="expanded$ | async" (expandedChange)="expandedChanged($event)">
      <mat-expansion-panel-header i18n="@@planner.appearance-options">
        Map appearance options
      </mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>
        <mat-radio-group [value]="mapMode$ | async" (change)="modeChanged($event)">
          <div>
            <mat-radio-button value="surface" class="mode-radio-button" i18n="@@planner.surface">
              Surface
            </mat-radio-button>
          </div>
          <div>
            <mat-radio-button value="survey" class="mode-radio-button" i18n="@@planner.survey">
              Date last survey
            </mat-radio-button>
          </div>
          <div>
            <mat-radio-button value="analysis" class="mode-radio-button" i18n="@@planner.quality">
              Node and route quality status
            </mat-radio-button>
          </div>
        </mat-radio-group>
      </ng-template>
    </mat-expansion-panel>
  `,
  standalone: true,
  imports: [MatExpansionModule, MatRadioModule, AsyncPipe],
})
export class PlannerSideBarAppearanceComponent {
  private readonly store = inject(Store);
  protected readonly mapMode$ = this.store.select(selectPlannerMapMode);
  protected readonly expanded$ = this.store.select(selectPreferencesShowAppearanceOptions);

  expandedChanged(expanded: boolean): void {
    this.store.dispatch(actionPreferencesShowAppearanceOptions({ value: expanded }));
  }

  modeChanged(event: MatRadioChange): void {
    this.store.dispatch(actionPlannerMapMode({ mapMode: event.value }));
  }
}
