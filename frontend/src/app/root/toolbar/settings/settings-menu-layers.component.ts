import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckbox } from '@angular/material/checkbox';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuItem } from '@angular/material/menu';
import { DividerComponent } from '@app/components/shared';
import { State } from '@app/state';

@Component({
  selector: 'kpn-settings-menu-layers',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div mat-menu-item>
      <mat-checkbox
        [checked]="standardBackgroundLayerEnabled()"
        (change)="standardBackgroundLayerEnabledChanged($event)"
        (click)="$event.stopPropagation()"
      >
        Standard background
      </mat-checkbox>
    </div>

    <div mat-menu-item>
      <mat-checkbox
        [checked]="osmBackgroundLayerEnabled()"
        (change)="osmBackgroundLayerEnabledChanged($event)"
        (click)="$event.stopPropagation()"
      >
        OSM background
      </mat-checkbox>
    </div>

    <kpn-divider />

    @if (routeType() == 'hiking') {
      <div mat-menu-item>
        <mat-checkbox
          [checked]="routeLayerEnabled()"
          (change)="routeLayerEnabledChanged($event)"
          (click)="$event.stopPropagation()"
          i18n="@@network-type.hiking"
        >
          Hiking
        </mat-checkbox>
      </div>
    }

    @if (routeType() == 'cycling') {
      <div mat-menu-item>
        <mat-checkbox
          [checked]="routeLayerEnabled()"
          (change)="routeLayerEnabledChanged($event)"
          (click)="$event.stopPropagation()"
          i18n="@@network-type.cycling"
        >
          Cycling
        </mat-checkbox>
      </div>
    }

    @if (routeType() == 'horse-riding') {
      <div mat-menu-item>
        <mat-checkbox
          [checked]="routeLayerEnabled()"
          (change)="routeLayerEnabledChanged($event)"
          (click)="$event.stopPropagation()"
          i18n="@@network-type.horse-riding"
        >
          Horseriding
        </mat-checkbox>
      </div>
    }

    @if (routeType() == 'motorboat') {
      <div mat-menu-item>
        <mat-checkbox
          [checked]="routeLayerEnabled()"
          (change)="routeLayerEnabledChanged($event)"
          (click)="$event.stopPropagation()"
          i18n="@@network-type.motorboat"
        >
          Motorboat
        </mat-checkbox>
      </div>
    }

    @if (routeType() == 'canoe') {
      <div mat-menu-item>
        <mat-checkbox
          [checked]="routeLayerEnabled()"
          (change)="routeLayerEnabledChanged($event)"
          (click)="$event.stopPropagation()"
          i18n="@@network-type.canoe"
        >
          Canoe
        </mat-checkbox>
      </div>
    }

    @if (routeType() == 'hiking') {
      <div mat-menu-item>
        <mat-checkbox
          [checked]="flandersOpenDataLayerEnabled()"
          (change)="flandersOpenDataLayerEnabledChanged($event)"
          (click)="$event.stopPropagation()"
          i18n="@@map.layer.flanders-hiking"
        >
          Toerisme Vlaanderen (hiking)
        </mat-checkbox>
      </div>
    }

    @if (routeType() == 'cycling') {
      <div mat-menu-item>
        <mat-checkbox
          [checked]="flandersOpenDataLayerEnabled()"
          (change)="flandersOpenDataLayerEnabledChanged($event)"
          (click)="$event.stopPropagation()"
          i18n="@@map.layer.flanders-cycling"
        >
          Toerisme Vlaanderen (cycling)
        </mat-checkbox>
      </div>
    }

    @if (routeType() == 'hiking') {
      <div mat-menu-item>
        <mat-checkbox
          [checked]="netherlandsOpenDataLayerEnabled()"
          (change)="netherlandsOpenDataLayerEnabledChanged($event)"
          (click)="$event.stopPropagation()"
          i18n="@@map.layer.netherlands-hiking"
        >
          NL routedatabank (hiking)
        </mat-checkbox>
      </div>
    }

    @if (routeType() == 'cycling') {
      <div mat-menu-item>
        <mat-checkbox
          [checked]="netherlandsOpenDataLayerEnabled()"
          (change)="netherlandsOpenDataLayerEnabledChanged($event)"
          (click)="$event.stopPropagation()"
          i18n="@@map.layer.netherlands-cycling"
        >
          NL routedatabank (cycling)
        </mat-checkbox>
      </div>
    }

    @if (routeType() == 'hiking') {
      <div mat-menu-item>
        <mat-checkbox
          [checked]="franceOpenDataLayerEnabled()"
          (change)="franceOpenDataLayerEnabledChanged($event)"
          (click)="$event.stopPropagation()"
          i18n="map.layer.france-hiking"
        >
          Parc du Vercors
        </mat-checkbox>
      </div>
    }

    <div mat-menu-item>
      <mat-checkbox
        [checked]="gridLayerEnabled()"
        (change)="gridLayerEnabledChanged($event)"
        (click)="$event.stopPropagation()"
      >
        Grid
      </mat-checkbox>
    </div>
  `,
  styles: ``,
  imports: [MatButtonModule, MatIconModule, MatMenuItem, MatCheckbox, DividerComponent],
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

  standardBackgroundLayerEnabledChanged(event: MatCheckboxChange): void {
    this.layers.updateStandardBackgroundLayerEnabled(event.checked);
  }

  osmBackgroundLayerEnabledChanged(event: MatCheckboxChange): void {
    this.layers.updateOsmBackgroundLayerEnabled(event.checked);
  }

  flandersOpenDataLayerEnabledChanged(event: MatCheckboxChange): void {
    this.layers.updateFlandersOpenDataLayerEnabled(event.checked);
  }

  netherlandsOpenDataLayerEnabledChanged(event: MatCheckboxChange): void {
    this.layers.updateNetherlandsOpenDataLayerEnabled(event.checked);
  }

  franceOpenDataLayerEnabledChanged(event: MatCheckboxChange): void {
    this.layers.updateFranceOpenDataLayerEnabled(event.checked);
  }

  routeLayerEnabledChanged(event: MatCheckboxChange): void {
    this.layers.updateRouteLayerEnabled(event.checked);
  }

  gridLayerEnabledChanged(event: MatCheckboxChange): void {
    this.layers.updateGridLayerEnabled(event.checked);
  }
}
