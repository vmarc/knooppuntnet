import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { State } from '@app/state/state';
import { MenuItemCheckboxComponent } from './menu-item-checkbox.component';

@Component({
  selector: 'kpn-settings-menu-poi-option',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-menu-item-checkbox
      [value]="groupEnabled()"
      [disabled]="poiLayerEnabled() === false"
      (toggle)="groupEnabledChanged()"
      [label]="label()"
    />
  `,
  imports: [MenuItemCheckboxComponent],
})
export class SettingsMenuPoiOptionComponent {
  private readonly state = inject(State);
  readonly label = input.required<string>();
  readonly groupName = input.required<string>();
  readonly groupEnabled = computed(() => this.state.map.poiActive().get(this.groupName()));
  readonly poiLayerEnabled = this.state.map.layers.poiLayerEnabled;

  groupEnabledChanged(): void {
    const value = this.groupEnabled();
    this.state.map.updatePoiGroupActive(this.groupName(), !value);
  }
}
