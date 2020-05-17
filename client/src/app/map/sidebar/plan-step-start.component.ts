import {ChangeDetectionStrategy, Component} from "@angular/core";
import {FormControl} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {Observable} from "rxjs";
import {WarningDialogComponent} from "../../components/shared/dialog/warning-dialog.component";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-plan-step-start",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul>
      <li>
        Manually zoom in and click the node that you want to be the start of your route.
      </li>
      <li>
        <div class="start-option">
          <span>OR</span>
          <button
            mat-stroked-button
            (click)="zoomInOnCurrentLocation()">
            Zoom in on current location
          </button>
        </div>
      </li>

      <li>
        <div class="start-option">
          <span>OR</span>

          <form class="example-form">
            <input type="text"
                   placeholder="Enter location name"
                   [matAutocomplete]="auto">
            <mat-autocomplete #auto="matAutocomplete">
              <mat-option *ngFor="let street of filteredStreets | async" [value]="street">
                {{street}}
              </mat-option>
            </mat-autocomplete>
          </form>
        </div>
      </li>
    </ul>
  `,
  styles: [`
    li {
      padding-top: 0.5em;
      padding-bottom: 0.5em;
    }

    input {
      font-size: 1em;
      line-height: 1.8em;
    }

    .start-option {
      display: flex;
      align-items: baseline;
    }

    .start-option :first-child {
      padding-right: 1em;
    }
  `]
})
export class PlanStepStartComponent {

  control = new FormControl();
  streets: string[] = ["Champs-Élysées", "Lombard Street", "Abbey Road", "Fifth Avenue"];
  filteredStreets: Observable<string[]>;

  constructor(private dialog: MatDialog) {
  }

  zoomInOnCurrentLocation(): void {

    let message = "This action will try to zoom in on the current location of the user. ";
    message += "This action will use the 'Geolocation API'. ";
    message += "For privacy reasons, the user will be asked for permission to report location information. ";
    message += "This action has not been implemented yet.";

    this.dialog.open(
      WarningDialogComponent,
      {
        width: "450px",
        data: {
          title: "Current location - not implemented yet",
          message: message
        }
      }
    );
  }
}
