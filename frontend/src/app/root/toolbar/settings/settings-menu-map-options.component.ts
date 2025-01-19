import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { State } from '@app/state';
import { TuiDataListComponent } from '@taiga-ui/core';
import { TuiDataListDropdownManager } from '@taiga-ui/kit';
import { ExploreMode } from '../../../explore/explore-mode';
import { MenuItemRadioComponent } from './menu-item-radio.component';

@Component({
  selector: 'kpn-settings-menu-map-options',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <tui-data-list tuiDataListDropdownManager size="m">
      <kpn-menu-item-radio
        name="mode"
        label="Standard"
        value="standard"
        [actual]="mode()"
        (changed)="modeChanged('standard')"
      />
      <kpn-menu-item-radio
        name="mode"
        label="Surface"
        value="surface"
        [actual]="mode()"
        (changed)="modeChanged('surface')"
      />
      <kpn-menu-item-radio
        name="mode"
        label="Date last survey"
        value="survey"
        [actual]="mode()"
        (changed)="modeChanged('survey')"
      />
      <kpn-menu-item-radio
        name="mode"
        label="Quality status"
        value="analysis"
        [actual]="mode()"
        (changed)="modeChanged('analysis')"
      />
    </tui-data-list>
    <ng-template #content let-data>
      @switch (data) {
        @case ('standard') {
          <span i18n="@@planner.standard">Standard</span>
        }
        @case ('surface') {
          <span i18n="@@planner.surface">Surface</span>
        }
        @case ('survey') {
          <span i18n="@@planner.survey">Date last survey</span>
        }
        @case ('analysis') {
          <span i18n="@@planner.quality">Quality status</span>
        }
      }
    </ng-template>
  `,
  imports: [FormsModule, MenuItemRadioComponent, TuiDataListComponent, TuiDataListDropdownManager],
})
export class SettingsMenuMapOptionsComponent {
  private readonly state = inject(State);
  readonly mode = this.state.map.mode;

  modeChanged(modeOption: ExploreMode): void {
    this.state.map.updateMode(modeOption);
  }
}
