import {Component} from '@angular/core';

@Component({
  selector: 'kpn-map-sidebar-appearance',
  template: `
    <mat-expansion-panel>
      <mat-expansion-panel-header>
        Map appearance options
      </mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>
        <mat-radio-group [value]="analysisMode">
          <mat-radio-button value="planner" class="mode-radio-button">
            Surface
          </mat-radio-button>
          <mat-radio-button value="status" class="mode-radio-button">
            Node and route quality status
          </mat-radio-button>
          <mat-radio-button value="survey" class="mode-radio-button">
            Date last survey
          </mat-radio-button>
        </mat-radio-group>
      </ng-template>
    </mat-expansion-panel>
  `
})
export class MapSidebarAppearanceComponent {
  analysisMode = "planner";
}
