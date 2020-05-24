import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";
import {MatRadioChange} from "@angular/material/radio";

@Component({
  selector: "kpn-plan-output-dialog",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <h2 mat-dialog-title>
      <mat-icon svgIcon="output"></mat-icon>
      <span i18n="@@plan.output.title">Output</span>
    </h2>
    <mat-dialog-content>
      <mat-radio-group [value]="exportString()" (change)="exportChanged($event)">
        <mat-radio-button value="pdf1" i18n="@@plan.output.compact-pdf">
          Compact
        </mat-radio-button>
        <mat-radio-button value="pdf2" i18n="@@plan.output.node-strip-pdf">
          Node strip
        </mat-radio-button>
        <mat-radio-button value="pdf3" i18n="@@plan.output.navigation-instructions-pdf">
          Navigation instructions
        </mat-radio-button>
        <mat-radio-button value="gpx" i18n="@@plan.output.gpx">
          GPX file
        </mat-radio-button>
      </mat-radio-group>
    </mat-dialog-content>
    <mat-dialog-actions>
      <button mat-stroked-button mat-dialog-close i18n="@@plan.output.cancel">Cancel</button>
      <button mat-stroked-button [mat-dialog-close]="export" cdkFocusInitial i18n="@@plan.output.ok">OK</button>
    </mat-dialog-actions>
  `,
  styles: [`

    h2 {
      display: flex;
      align-items: center;
    }

    h2 mat-icon {
      padding-right: 10px;
    }

    mat-radio-group {
      display: flex;
      flex-direction: column;
      margin: 15px 0;
    }

    mat-radio-button {
      margin: 5px;
    }

    mat-dialog-actions :not(:last-child) {
      margin-right: 10px;
    }

  `]
})
export class PlanOutputDialogComponent {

  export = "pdf1";

  exportString(): string {
    return this.export;
  }

  exportChanged(event: MatRadioChange) {
    this.export = event.value;
  }

}
