import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckboxChange } from '@angular/material/checkbox';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { DividerComponent } from '@app/components/shared';
import { ChangeDetectionStrategy } from '@angular/core';
import { State } from '@app/state';

@Component({
  selector: 'kpn-explore-layers',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-divider />

    <mat-checkbox
      [checked]="standardBackgroundLayerEnabled()"
      (change)="standardBackgroundLayerEnabledChanged($event)"
    >
      Standard background
    </mat-checkbox>

    <mat-checkbox
      [checked]="osmBackgroundLayerEnabled()"
      (change)="osmBackgroundLayerEnabledChanged($event)"
    >
      OSM background
    </mat-checkbox>

    <kpn-divider />

    @if (networkType() == 'hiking') {
      <mat-checkbox
        [checked]="routeLayerEnabled()"
        (change)="routeLayerEnabledChanged($event)"
        i18n="@@network-type.hiking"
      >
        Hiking
      </mat-checkbox>
    }
    @if (networkType() == 'cycling') {
      <mat-checkbox
        [checked]="routeLayerEnabled()"
        (change)="routeLayerEnabledChanged($event)"
        i18n="@@network-type.cycling"
      >
        Cycling
      </mat-checkbox>
    }
    @if (networkType() == 'horse-riding') {
      <mat-checkbox
        [checked]="routeLayerEnabled()"
        (change)="routeLayerEnabledChanged($event)"
        i18n="@@network-type.horse-riding"
      >
        Horseriding
      </mat-checkbox>
    }
    @if (networkType() == 'motorboat') {
      <mat-checkbox
        [checked]="routeLayerEnabled()"
        (change)="routeLayerEnabledChanged($event)"
        i18n="@@network-type.motorboat"
      >
        Motorboat
      </mat-checkbox>
    }
    @if (networkType() == 'canoe') {
      <mat-checkbox
        [checked]="routeLayerEnabled()"
        (change)="routeLayerEnabledChanged($event)"
        i18n="@@network-type.canoe"
      >
        Canoe
      </mat-checkbox>
    }

    @if (networkType() == 'hiking') {
      <mat-checkbox
        [checked]="flandersOpenDataLayerEnabled()"
        (change)="flandersOpenDataLayerEnabledChanged($event)"
        i18n="@@map.layer.flanders-hiking"
      >
        Toerisme Vlaanderen (hiking)
      </mat-checkbox>
    }
    @if (networkType() == 'cycling') {
      <mat-checkbox
        [checked]="flandersOpenDataLayerEnabled()"
        (change)="flandersOpenDataLayerEnabledChanged($event)"
        i18n="@@map.layer.flanders-cycling"
      >
        Toerisme Vlaanderen (cycling)
      </mat-checkbox>
    }
    @if (networkType() == 'hiking') {
      <mat-checkbox
        [checked]="netherlandsOpenDataLayerEnabled()"
        (change)="netherlandsOpenDataLayerEnabledChanged($event)"
        i18n="@@map.layer.netherlands-hiking"
      >
        NL routedatabank (hiking)
      </mat-checkbox>
    }
    @if (networkType() == 'cycling') {
      <mat-checkbox
        [checked]="netherlandsOpenDataLayerEnabled()"
        (change)="netherlandsOpenDataLayerEnabledChanged($event)"
        i18n="@@map.layer.netherlands-cycling"
      >
        NL routedatabank (cycling)
      </mat-checkbox>
    }
    @if (networkType() == 'hiking') {
      <mat-checkbox
        [checked]="franceOpenDataLayerEnabled()"
        (change)="franceOpenDataLayerEnabledChanged($event)"
        i18n="map.layer.france-hiking"
      >
        Parc du Vercors
      </mat-checkbox>
    }
    <mat-checkbox [checked]="gridLayerEnabled()" (change)="gridLayerEnabledChanged($event)">
      Grid
    </mat-checkbox>
  `,
  styles: `
    mat-checkbox {
      display: block;
      padding-right: 20px;
    }
  `,
  standalone: true,
  imports: [MatCheckboxModule, DividerComponent],
})
export class ExploreLayersComponent {
  private readonly state = inject(State);
  protected networkType = this.state.page.networkType;
  protected standardBackgroundLayerEnabled = this.state.map.standardBackgroundLayerEnabled;
  protected osmBackgroundLayerEnabled = this.state.map.osmBackgroundLayerEnabled;
  protected flandersOpenDataLayerEnabled = this.state.map.flandersOpenDataLayerEnabled;
  protected netherlandsOpenDataLayerEnabled = this.state.map.netherlandsOpenDataLayerEnabled;
  protected franceOpenDataLayerEnabled = this.state.map.franceOpenDataLayerEnabled;
  protected routeLayerEnabled = this.state.map.routeLayerEnabled;
  protected gridLayerEnabled = this.state.map.gridLayerEnabled;

  standardBackgroundLayerEnabledChanged(event: MatCheckboxChange): void {
    this.state.map.updateStandardBackgroundLayerEnabled(event.checked);
  }

  osmBackgroundLayerEnabledChanged(event: MatCheckboxChange): void {
    this.state.map.updateOsmBackgroundLayerEnabled(event.checked);
  }

  flandersOpenDataLayerEnabledChanged(event: MatCheckboxChange): void {
    this.state.map.updateFlandersOpenDataLayerEnabled(event.checked);
  }

  netherlandsOpenDataLayerEnabledChanged(event: MatCheckboxChange): void {
    this.state.map.updateNetherlandsOpenDataLayerEnabled(event.checked);
  }

  franceOpenDataLayerEnabledChanged(event: MatCheckboxChange): void {
    this.state.map.updateFranceOpenDataLayerEnabled(event.checked);
  }

  routeLayerEnabledChanged(event: MatCheckboxChange): void {
    this.state.map.updateRouteLayerEnabled(event.checked);
  }

  gridLayerEnabledChanged(event: MatCheckboxChange): void {
    this.state.map.updateGridLayerEnabled(event.checked);
  }
}
