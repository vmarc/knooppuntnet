import {Component} from "@angular/core";
import {MatRadioChange} from "@angular/material";

@Component({
  selector: "kpn-export-dialog",
  template: `
    <h2 mat-dialog-title>Export</h2>
    <mat-dialog-content>
      <mat-radio-group [value]="exportString()" (change)="exportChanged($event)">
        <mat-radio-button value="pdf1">
          Nodes horizontally (pdf)
        </mat-radio-button>
        <mat-radio-button value="pdf2">
          Nodes vertically (pdf)
        </mat-radio-button>
        <mat-radio-button value="pdf3">
          Navigation instructions (pdf)
        </mat-radio-button>
        <mat-radio-button value="gpx">
          GPX file
        </mat-radio-button>
      </mat-radio-group>
    </mat-dialog-content>
    <mat-dialog-actions>
      <button mat-stroked-button mat-dialog-close>Cancel</button>
      <button mat-stroked-button [mat-dialog-close]="export" cdkFocusInitial>OK</button>
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

  export: string = "pdf1";

  exportString(): string {
    return this.export;
  }

  exportChanged(event: MatRadioChange) {
    this.export = event.value;
  }

}
