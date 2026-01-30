import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Translations } from '@app/shared/i18n/translations';
import { State } from '@app/state/state';
import { MenuItemCheckboxComponent } from './menu-item-checkbox.component';
import { SettingsMenuPoiComponent } from './settings-menu-poi.component';

@Component({
  selector: 'ui-settings-menu-layers',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-menu-items">
      <div>
        <ui-menu-item-checkbox
          [value]="backgroundLayerEnabled()"
          (toggle)="toggleBackgroundLayerEnabled()"
          i18n-label="@@map.layer.osm-background"
          label="OSM background"
        />
      </div>
      <div>
        <ui-menu-item-checkbox
          [value]="routeLayerEnabled()"
          (toggle)="toggleRouteLayerEnabled()"
          [label]="routeTypeLabel()"
        />

        @if (routeType() == 'hiking' || routeType() == 'cycling') {
          <ui-menu-item-checkbox
            [value]="flandersOpenDataLayerEnabled()"
            (toggle)="toggleFlandersOpenDataLayerEnabled()"
            i18n-label="@@map.layer.open-data-flanders"
            label="Toerisme Vlaanderen"
          />
        }

        @if (routeType() == 'hiking' || routeType() == 'cycling') {
          <ui-menu-item-checkbox
            [value]="netherlandsOpenDataLayerEnabled()"
            (toggle)="toggleNetherlandsOpenDataLayerEnabled()"
            i18n-label="@@map.layer.open-data-netherlands"
            label="NL routedatabank"
          />
        }

        @if (routeType() == 'hiking') {
          <ui-menu-item-checkbox
            [value]="franceOpenDataLayerEnabled()"
            (toggle)="toggleFranceOpenDataLayerEnabled()"
            i18n-label="@@map.layer.open-data-france"
            label="Parc du Vercors"
          />
        }

        <ui-menu-item-checkbox
          [value]="gridLayerEnabled()"
          (toggle)="toggleGridLayerEnabled()"
          i18n-label="@@map.layer.grid"
          label="Grid"
        />
      </div>

      <div>
        <ui-settings-menu-poi />
      </div>
    </div>
  `,
  imports: [MenuItemCheckboxComponent, SettingsMenuPoiComponent],
})
export class SettingsMenuLayersComponent {
  private readonly state = inject(State);
  private readonly layers = this.state.map.layers;
  protected readonly routeType = this.state.page.routeType;
  protected readonly backgroundLayerEnabled = this.layers.backgroundLayerEnabled;
  protected readonly flandersOpenDataLayerEnabled = this.layers.flandersOpenDataLayerEnabled;
  protected readonly netherlandsOpenDataLayerEnabled = this.layers.netherlandsOpenDataLayerEnabled;
  protected readonly franceOpenDataLayerEnabled = this.layers.franceOpenDataLayerEnabled;
  protected readonly routeLayerEnabled = this.layers.routeLayerEnabled;
  protected readonly gridLayerEnabled = this.layers.gridLayerEnabled;

  protected readonly routeTypeLabel = computed(() => Translations.routeTypeLabel(this.routeType()));

  toggleBackgroundLayerEnabled(): void {
    const value = this.backgroundLayerEnabled();
    this.layers.updateBackgroundLayerEnabled(!value);
  }

  toggleFlandersOpenDataLayerEnabled(): void {
    const value = this.flandersOpenDataLayerEnabled();
    this.layers.updateFlandersOpenDataLayerEnabled(!value);
  }

  toggleNetherlandsOpenDataLayerEnabled(): void {
    const value = this.netherlandsOpenDataLayerEnabled();
    this.layers.updateNetherlandsOpenDataLayerEnabled(!value);
  }

  toggleFranceOpenDataLayerEnabled(): void {
    const value = this.franceOpenDataLayerEnabled();
    this.layers.updateFranceOpenDataLayerEnabled(!value);
  }

  toggleRouteLayerEnabled(): void {
    const value = this.routeLayerEnabled();
    this.layers.updateRouteLayerEnabled(!value);
  }

  toggleGridLayerEnabled(): void {
    const value = this.gridLayerEnabled();
    this.layers.updateGridLayerEnabled(!value);
  }
}
