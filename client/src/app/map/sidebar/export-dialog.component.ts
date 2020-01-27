import {Component} from "@angular/core";
import {MatRadioChange} from "@angular/material";

@Component({
  selector: "kpn-export-dialog",
  template: `
    <h2 mat-dialog-title i18n="@@export.title">Export</h2>
    <mat-dialog-content>
      <mat-radio-group [value]="exportString()" (change)="exportChanged($event)">
        <mat-radio-button value="pdf1" i18n="@@export.default-pdf">
          Print (pdf)
        </mat-radio-button>
        <mat-radio-button value="pdf2" i18n="@@export.node-strip-pdf">
          Node strip (pdf)
        </mat-radio-button>
        <mat-radio-button value="pdf3" i18n="@@export.navigation-instructions-pdf">
          Navigation instructions (pdf)
        </mat-radio-button>
        <mat-radio-button value="gpx" i18n="@@export.gpx">
          GPX file
        </mat-radio-button>
      </mat-radio-group>
    </mat-dialog-content>
    <mat-dialog-actions>
      <button mat-stroked-button mat-dialog-close i18n="@@export.cancel">Cancel</button>
      <button mat-stroked-button [mat-dialog-close]="export" cdkFocusInitial i18n="@@export.ok">OK</button>
    </mat-dialog-actions>
  `,
  styles: [`

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
export class ExportDialogComponent {

  export = "pdf1";

  exportString(): string {
    return this.export;
  }

  exportChanged(event: MatRadioChange) {
    this.export = event.value;
  }

}
