import {ChangeDetectionStrategy, Component} from "@angular/core";
import {MatButtonToggleChange} from "@angular/material/button-toggle";
import {MatDialog} from "@angular/material/dialog";
import {Router} from "@angular/router";
import {WarningDialogComponent} from "../../components/shared/dialog/warning-dialog.component";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-network-type-selector",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-button-toggle-group [value]="'cycling'" (change)="networkTypeChanged($event)">
      <mat-button-toggle value="cycling">
        <mat-icon svgIcon="cycling"></mat-icon>
      </mat-button-toggle>
      <mat-button-toggle value="hiking">
        <mat-icon svgIcon="hiking"></mat-icon>
      </mat-button-toggle>
      <mat-button-toggle value="horse riding">
        <mat-icon svgIcon="horse-riding"></mat-icon>
      </mat-button-toggle>
      <mat-button-toggle value="motorboat">
        <mat-icon svgIcon="motorboat"></mat-icon>
      </mat-button-toggle>
      <mat-button-toggle value="canoe">
        <mat-icon svgIcon="canoe"></mat-icon>
      </mat-button-toggle>
      <mat-button-toggle value="inline skating">
        <mat-icon svgIcon="inline-skating"></mat-icon>
      </mat-button-toggle>
    </mat-button-toggle-group>
  `,
  styles: [`
    mat-button-toggle-group {
      top: 4px;
      height: 40px;
    }
    mat-icon {
      height: 15px;
      width: 15px;
    }
  `]
})
export class NetworkTypeSelectorComponent {

  constructor(private router: Router,
              private dialog: MatDialog) {
  }

  networkTypeChanged(event: MatButtonToggleChange) {

    // this.router.navigate(["/map/" + event.value]);

    let message = `This action will switch the map to ${event.value}. `;
    message += `This action has not been implemented yet.`;
    this.dialog.open(
      WarningDialogComponent,
      {
        width: "450px",
        data: {
          title: "Change network type",
          message: message
        }
      }
    );
  }
}
