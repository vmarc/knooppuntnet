import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NzRadioGroupComponent } from 'ng-zorro-antd/radio';
import { NzRadioComponent } from 'ng-zorro-antd/radio';
import { LocationModeService } from './location-mode.service';
import { LocationSelectionMode } from './location-selection-mode';

@Component({
  selector: 'ui-location-mode',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div i18n="@@analysis.location-side-bar.title">Location</div>

    <nz-radio-group [ngModel]="mode()" (ngModelChange)="modeChanged($event)">
      <li>
        <label nz-radio nzValue="name" i18n="@@analysis.location.search-by-name">
          Search by name
        </label>
      </li>
      <li>
        <label nz-radio nzValue="tree" i18n="@@analysis.location.select-from-tree">
          Select from tree
        </label>
      </li>
    </nz-radio-group>
  `,
  styles: `
    :host {
      display: block;
    }
  `,
  imports: [NzRadioComponent, NzRadioGroupComponent, FormsModule],
})
export class LocationModeComponent {
  private readonly locationModeService = inject(LocationModeService);
  readonly mode = this.locationModeService.mode;

  modeChanged(value: LocationSelectionMode) {
    this.locationModeService.setMode(value);
  }
}
