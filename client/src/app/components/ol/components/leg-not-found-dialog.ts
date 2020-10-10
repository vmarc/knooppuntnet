import {ChangeDetectionStrategy, Component} from "@angular/core";

@Component({
  selector: "kpn-leg-not-found-dialog",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title i18n="@@leg-not-found-dialog.title">
        No path
      </div>
      <div mat-dialog-content i18n="@@leg-not-found-dialog.message">
        The planner did not find a path to the selected destination.
      </div>
    </kpn-dialog>
  `
})
export class LegNotFoundDialogComponent {
}
