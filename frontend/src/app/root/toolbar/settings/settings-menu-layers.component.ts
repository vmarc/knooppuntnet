import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { State } from '@app/state';
import { MenuItemCheckboxComponent } from './menu-item-checkbox.component';
import { SettingsMenuPoiComponent } from './settings-menu-poi.component';

@Component({
  selector: 'kpn-settings-menu-layers',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-menu-items">
      <div>
        <kpn-menu-item-checkbox
          [value]="standardBackgroundLayerEnabled()"
          (toggle)="toggleStandardBackgroundLayerEnabled()"
          label="Standard background"
        />
        <kpn-menu-item-checkbox
          [value]="osmBackgroundLayerEnabled()"
          (toggle)="toggleOsmBackgroundLayerEnabled()"
          label="OSM background"
        />
      </div>
      <div>
        @if (routeType() == 'hiking') {
          <kpn-menu-item-checkbox
            [value]="routeLayerEnabled()"
            (toggle)="toggleRouteLayerEnabled()"
            i18n-label="@@route-type.hiking"
            label="Hiking"
          />
        }

        @if (routeType() == 'cycling') {
          <kpn-menu-item-checkbox
            [value]="routeLayerEnabled()"
            (toggle)="toggleRouteLayerEnabled()"
            i18n-label="@@network-type.cycling"
            label="Cycling"
          />
        }

        @if (routeType() == 'horse-riding') {
          <kpn-menu-item-checkbox
            [value]="routeLayerEnabled()"
            (toggle)="toggleRouteLayerEnabled()"
            i18n-label="@@network-type.horse-riding"
            label="Horseriding"
          />
        }

        @if (routeType() == 'motorboat') {
          <kpn-menu-item-checkbox
            [value]="routeLayerEnabled()"
            (toggle)="toggleRouteLayerEnabled()"
            i18n-label="@@network-type.motorboat"
            label="Motorboat"
          />
        }

        @if (routeType() == 'canoe') {
          <kpn-menu-item-checkbox
            [value]="routeLayerEnabled()"
            (toggle)="toggleRouteLayerEnabled()"
            i18n-label="@@network-type.canoe"
            label="Canoe"
          />
        }

        @if (routeType() == 'hiking') {
          <kpn-menu-item-checkbox
            [value]="flandersOpenDataLayerEnabled()"
            (toggle)="toggleFlandersOpenDataLayerEnabled()"
            i18n-label="@@map.layer.flanders-hiking"
            label="Toerisme Vlaanderen (hiking)"
          />
        }

        @if (routeType() == 'cycling') {
          <kpn-menu-item-checkbox
            [value]="flandersOpenDataLayerEnabled()"
            (toggle)="toggleFlandersOpenDataLayerEnabled()"
            i18n-label="@@map.layer.flanders-cycling"
            label="Toerisme Vlaanderen (cycling)"
          />
        }

        @if (routeType() == 'hiking') {
          <kpn-menu-item-checkbox
            [value]="netherlandsOpenDataLayerEnabled()"
            (toggle)="toggleNetherlandsOpenDataLayerEnabled()"
            i18n-label="@@map.layer.netherlands-hiking"
            label="NL routedatabank (hiking)"
          />
        }

        @if (routeType() == 'cycling') {
          <kpn-menu-item-checkbox
            [value]="netherlandsOpenDataLayerEnabled()"
            (toggle)="toggleNetherlandsOpenDataLayerEnabled()"
            i18n-label="@@map.layer.netherlands-cycling"
            label="NL routedatabank (cycling)"
          />
        }

        @if (routeType() == 'hiking') {
          <kpn-menu-item-checkbox
            [value]="franceOpenDataLayerEnabled()"
            (toggle)="toggleFranceOpenDataLayerEnabled()"
            label="Parc du Vercors"
          />
        }

        <kpn-menu-item-checkbox
          [value]="gridLayerEnabled()"
          (toggle)="toggleGridLayerEnabled()"
          label="Grid"
        />
      </div>

      <div>
        <kpn-settings-menu-poi />
      </div>
    </div>
  `,
  imports: [MenuItemCheckboxComponent, SettingsMenuPoiComponent],
})
export class SettingsMenuLayersComponent {
  private readonly state = inject(State);
  private readonly layers = this.state.map.layers;
  readonly routeType = this.state.page.routeType;
  readonly standardBackgroundLayerEnabled = this.layers.standardBackgroundLayerEnabled;
  readonly osmBackgroundLayerEnabled = this.layers.osmBackgroundLayerEnabled;
  readonly flandersOpenDataLayerEnabled = this.layers.flandersOpenDataLayerEnabled;
  readonly netherlandsOpenDataLayerEnabled = this.layers.netherlandsOpenDataLayerEnabled;
  readonly franceOpenDataLayerEnabled = this.layers.franceOpenDataLayerEnabled;
  readonly routeLayerEnabled = this.layers.routeLayerEnabled;
  readonly gridLayerEnabled = this.layers.gridLayerEnabled;

  toggleStandardBackgroundLayerEnabled(): void {
    const value = this.standardBackgroundLayerEnabled();
    this.layers.updateStandardBackgroundLayerEnabled(!value);
  }

  toggleOsmBackgroundLayerEnabled(): void {
    console.log(`TOGGLE OSM ${this.osmBackgroundLayerEnabled()}`);
    const value = this.osmBackgroundLayerEnabled();
    this.layers.updateOsmBackgroundLayerEnabled(!value);
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
