import {Component} from "@angular/core";
import {MatRadioChange} from "@angular/material/radio";
import {AnalysisModeService} from "./analysis-mode.service";

@Component({
  selector: "kpn-analysis-mode",
  template: `
    <mat-radio-group [value]="mode()" (change)="modeChanged($event)">
      <mat-radio-button
        value="network"
        title="Network"
        i18n="@@analysis.by-network">
        Explore by network
      </mat-radio-button>
      <mat-radio-button
        value="location"
        title="Location"
        i18n="@@analysis.by-location">
        Explore by location
      </mat-radio-button>
    </mat-radio-group>
  `,
  styles: [`
    mat-radio-group {
      margin-top: 10px;
      margin-bottom: 10px;
      display: block;
    }
    mat-radio-button {
      display: block;
      padding-bottom: 10px;
    }
  `]
})
export class AnalysisModeComponent {

  constructor(private analysisModeService: AnalysisModeService) {
  }

  mode() {
    return this.analysisModeService.currentMode();
  }

  modeChanged(event: MatRadioChange) {
    this.analysisModeService.setMode(event.value);
  }

}
