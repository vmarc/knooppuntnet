import { signal } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatRadioChange } from '@angular/material/radio';
import { MatRadioModule } from '@angular/material/radio';
import { PreferencesService } from '@app/core';

@Component({
  selector: 'kpn-configuration-mode',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-expansion-panel [expanded]="expanded()" (expandedChange)="expandedChanged($event)">
      <mat-expansion-panel-header i18n="@@planner.appearance-options">
        Map appearance options
      </mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>
        <mat-radio-group [value]="mapMode()" (change)="modeChanged($event)">
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
export class ConfigurationModeComponent {
  private readonly preferencesService = inject(PreferencesService);
  protected readonly mapMode = signal<string>('surface');
  protected readonly expanded = this.preferencesService.showAppearanceOptions;

  expandedChanged(expanded: boolean): void {
    this.preferencesService.setShowAppearanceOptions(expanded);
  }

  modeChanged(event: MatRadioChange): void {
    this.mapMode.set(event.value);
  }
}
