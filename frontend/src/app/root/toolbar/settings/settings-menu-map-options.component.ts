import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatLabel } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuItem } from '@angular/material/menu';
import { MatRadioChange } from '@angular/material/radio';
import { MatRadioGroup } from '@angular/material/radio';
import { MatRadioButton } from '@angular/material/radio';
import { State } from '@app/state';

@Component({
  selector: 'kpn-settings-menu-map-options',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-radio-group
      [value]="mode()"
      (change)="modeChanged($event)"
      (click)="$event.stopPropagation()"
    >
      <div mat-menu-item>
        <mat-radio-button value="standard" class="mode-radio-button" i18n="@@planner.standard">
          Standard
        </mat-radio-button>
      </div>
      <div mat-menu-item>
        <mat-radio-button value="surface" class="mode-radio-button" i18n="@@planner.surface">
          Surface
        </mat-radio-button>
      </div>
      <div mat-menu-item>
        <mat-radio-button value="survey" class="mode-radio-button" i18n="@@planner.survey">
          <mat-label>Date last survey</mat-label>
        </mat-radio-button>
      </div>
      <div mat-menu-item>
        <mat-radio-button value="analysis" class="mode-radio-button" i18n="@@planner.quality">
          Quality status
        </mat-radio-button>
      </div>
    </mat-radio-group>
  `,
  imports: [MatButtonModule, MatIconModule, MatRadioButton, MatRadioGroup, MatLabel, MatMenuItem],
})
export class SettingsMenuMapOptionsComponent {
  private readonly state = inject(State);
  readonly mode = this.state.map.mode;

  modeChanged(event: MatRadioChange): void {
    this.state.map.updateMode(event.value);
  }
}
