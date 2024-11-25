import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatRadioChange } from '@angular/material/radio';
import { MatRadioModule } from '@angular/material/radio';
import { State } from '@app/state';

@Component({
  selector: 'kpn-explore-mode',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-expansion-panel [expanded]="expanded()" (expandedChange)="expandedChanged($event)">
      <mat-expansion-panel-header i18n="@@planner.appearance-options">
        Map appearance options
      </mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>
        <mat-radio-group [value]="mode()" (change)="modeChanged($event)">
          <div>
            <mat-radio-button value="standard" class="mode-radio-button" i18n="@@planner.standard">
              Standard
            </mat-radio-button>
          </div>
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
              Quality status
            </mat-radio-button>
          </div>
        </mat-radio-group>
      </ng-template>
    </mat-expansion-panel>
  `,
  standalone: true,
  imports: [MatExpansionModule, MatRadioModule],
})
export class ExploreModeComponent {
  private readonly state = inject(State);
  protected readonly mode = this.state.map.mode;

  expanded(): boolean {
    // TODO redesign - read preference
    return true;
  }

  expandedChanged(expanded: boolean): void {
    // TODO redesign - store preference
  }

  modeChanged(event: MatRadioChange): void {
    this.state.map.updateMode(event.value);
  }
}
