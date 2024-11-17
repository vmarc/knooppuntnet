import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatCheckbox } from '@angular/material/checkbox';
import { MatExpansionPanelHeader } from '@angular/material/expansion';
import { MatExpansionPanelContent } from '@angular/material/expansion';
import { MatExpansionPanel } from '@angular/material/expansion';
import { DividerComponent } from '@app/components/shared';

@Component({
  selector: 'kpn-configuration-layers',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-expansion-panel [expanded]="expanded()" (expandedChange)="expandedChanged($event)">
      <mat-expansion-panel-header> Layers</mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>
        <mat-checkbox>Node network routes</mat-checkbox>
        <mat-checkbox>Non node network routes</mat-checkbox>
        <mat-checkbox>Toerisme Vlaanderen</mat-checkbox>
        <mat-checkbox>NL routedatabank</mat-checkbox>
        <mat-checkbox>Parc du Vercors</mat-checkbox>
        <kpn-divider />
        <mat-checkbox>OpenStreetMap</mat-checkbox>
        <mat-checkbox>Background</mat-checkbox>
        <mat-checkbox>Tilenames (256)</mat-checkbox>
        <mat-checkbox>Tilenames (512)</mat-checkbox>
        <kpn-divider />
        <mat-checkbox>Points of interest</mat-checkbox>
        <div class="checkbox-sub-group">
          <mat-checkbox>Hiking/biking</mat-checkbox>
          <mat-checkbox>Landmarks</mat-checkbox>
          <mat-checkbox>Restaurants</mat-checkbox>
          <mat-checkbox>Places to stay</mat-checkbox>
          <mat-checkbox>Tourism</mat-checkbox>
          <mat-checkbox>Amenity</mat-checkbox>
          <mat-checkbox>Shops</mat-checkbox>
          <mat-checkbox>Foodshops</mat-checkbox>
          <mat-checkbox>Sports</mat-checkbox>
        </div>
      </ng-template>
    </mat-expansion-panel>
  `,
  styles: [
    `
      mat-checkbox {
        display: block;
      }

      .checkbox-group {
        border-bottom: 1px solid lightgray;
      }

      .checkbox-sub-group {
        margin-left: 1em;
      }
    `,
  ],
  standalone: true,
  imports: [
    MatCheckbox,
    MatExpansionPanel,
    MatExpansionPanelContent,
    MatExpansionPanelHeader,
    DividerComponent,
  ],
})
export class ConfigurationLayersComponent {
  expanded(): boolean {
    return true;
  }

  expandedChanged(value: boolean): void {}
}
