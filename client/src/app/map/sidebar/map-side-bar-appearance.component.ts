import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";
import {MatRadioChange} from "@angular/material/radio";
import {MapService} from "../../components/ol/services/map.service";

@Component({
  selector: "kpn-map-sidebar-appearance",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-expansion-panel [expanded]="true">
      <mat-expansion-panel-header i18n="@@planner.appearance-options">
        Map appearance options
      </mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>
        <mat-radio-group [value]="mapMode()" (change)="modeChanged($event)">
          <mat-radio-button value="surface" class="mode-radio-button" i18n="@@planner.surface">
            Surface
          </mat-radio-button>
          <mat-radio-button value="survey" class="mode-radio-button" i18n="@@planner.survey">
            Date last survey
          </mat-radio-button>
          <mat-radio-button value="analysis" class="mode-radio-button" i18n="@@planner.quality">
            Node and route quality status
          </mat-radio-button>
        </mat-radio-group>
      </ng-template>
    </mat-expansion-panel>
  `,
  styles: [`
    mat-radio-button {
      display: block;
      margin: 10px;
    }
  `]
})
export class MapSidebarAppearanceComponent {

  constructor(private mapService: MapService) {
  }

  mapMode(): string {
    return this.mapService.mapMode();
  }

  modeChanged(event: MatRadioChange): void {
    this.mapService.nextMapMode(event.value);
  }
}
