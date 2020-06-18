import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";
import {MatRadioChange} from "@angular/material/radio";
import {GPX} from "ol/format";
import {MapMode} from "../../components/ol/services/map-mode";
import {MapService} from "../../components/ol/services/map.service";
import {PlannerLayerService} from "../planner/services/planner-layer.service";

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
        <div class="kpn-spacer-above">
          <input id="input-file-id" type="file" (change)="fileChanged($event)" class="file-input" accept=".gpx">
          <label
            for="input-file-id"
            class="mat-focus-indicator mat-stroked-button mat-button-base"
            i18n="@@planner.add-gpx_trace">
            Add your GPX trace
          </label>
        </div>
      </ng-template>
    </mat-expansion-panel>
  `,
  styles: [`
    mat-radio-button {
      display: block;
      margin: 10px;
    }

    .file-input {
      display: none;
    }

    .file-button {
      background-color: lightgreen;
    }

  `]
})
export class MapSidebarAppearanceComponent {

  constructor(private mapService: MapService,
              private plannerLayerService: PlannerLayerService) {
  }

  fileChanged(event) {
    const file = event.target.files[0];
    const fileReader = new FileReader();
    fileReader.onload = (e) => {
      const gpxFeatures = new GPX().readFeatures(fileReader.result, {featureProjection: "EPSG:3857"});
      this.plannerLayerService.gpxVectorLayer.getSource().addFeatures(gpxFeatures);
    };
    fileReader.readAsText(file);
  }

  mapMode(): MapMode {
    return this.mapService.mapMode();
  }

  modeChanged(event: MatRadioChange): void {
    this.mapService.nextMapMode(event.value);
  }
}
