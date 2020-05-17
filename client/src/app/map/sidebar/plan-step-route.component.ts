import {ChangeDetectionStrategy, Component} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {WarningDialogComponent} from "../../components/shared/dialog/warning-dialog.component";

/* tslint:disable:template-i18n English only */
@Component({
  selector: "kpn-plan-step-route",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul>
      <li>Click the endnode of your route.</li>
      <li>
        <span>OR</span>
        <span>Click several intermediate nodes and then the endnode of your route.</span>
      </li>
      <li><a (click)="more()">More...</a></li>
    </ul>
  `,
  styles: [`
    li {
      padding-top: 0.5em;
      padding-bottom: 0.5em;
    }

    li span:first-child {
      padding-right: 1em;
    }
  `]
})
export class PlanStepRouteComponent {

  constructor(private dialog: MatDialog) {
  }

  more(): void {

    let message = "This will be a link to that part of the documentation ";
    message += "that explains all possible interaction with the map while ";
    message += "planning a route. ";
    message += "This link has not been implemented yet.";

    this.dialog.open(
      WarningDialogComponent,
      {
        width: "450px",
        data: {
          title: "Link - not implemented yet",
          message: message
        }
      }
    );
  }
}
