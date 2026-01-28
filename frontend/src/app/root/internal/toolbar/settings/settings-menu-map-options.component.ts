import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MapMode } from '@app/mapold/domain/map-mode';
import { State } from '@app/state/state';
import { NzRadioComponent } from 'ng-zorro-antd/radio';
import { NzRadioGroupComponent } from 'ng-zorro-antd/radio';

@Component({
  selector: 'ui-settings-menu-map-options',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-radio-group [ngModel]="mode()" (ngModelChange)="modeChanged($event)">
      <li>
        <label nz-radio nzValue="standard" i18n="@@planner.standard">Standard</label>
      </li>
      <li>
        <label nz-radio nzValue="surface" i18n="@@planner.surface">Surface</label>
      </li>
      <li>
        <label nz-radio nzValue="survey" i18n="@@planner.survey">Date last survey</label>
      </li>
      <li>
        <label nz-radio nzValue="analysis" i18n="@@planner.quality">Quality status</label>
      </li>
    </nz-radio-group>
  `,
  imports: [FormsModule, NzRadioGroupComponent, NzRadioComponent],
})
export class SettingsMenuMapOptionsComponent {
  private readonly state = inject(State);
  readonly mode = this.state.map.mode;

  modeChanged(modeOption: MapMode): void {
    this.state.map.updateMode(modeOption);
  }
}
