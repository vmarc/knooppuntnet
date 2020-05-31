import {OnInit} from "@angular/core";
import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";
import {PdfService} from "../../pdf/pdf.service";
import {GpxWriter} from "../../pdf/plan/gpx-writer";
import {PlannerService} from "../planner.service";
import {PlanUtil} from "../planner/plan/plan-util";

@Component({
  selector: "kpn-plan-output-dialog",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title>
        <mat-icon svgIcon="output"></mat-icon>
        <span i18n="@@plan.output.title">Output</span>
      </div>
      <div mat-dialog-content class="dialog-content">
        <button
          mat-stroked-button
          (click)="printDocument()"
          title="Produce a route pdf file with compact node overview"
          i18n-title="@@plan.output.compact-pdf.tooltip"
          i18n="@@plan.output.compact-pdf">
          Compact
        </button>

        <button
          mat-stroked-button
          (click)="printStripDocument()"
          title="Produce a route pdf file with nodes in 'strip' format"
          i18n-title="@@plan.output.node-strip-pdf.tooltip"
          i18n="@@plan.output.node-strip-pdf">
          Node strip
        </button>

        <button
          mat-stroked-button
          (click)="printInstructions()"
          title="Produce a route pdf with navigation instructions"
          i18n-title="@@plan.output.navigation-instructions-pdf.tooltip"
          i18n="@@plan.output.navigation-instructions-pdf">
          Navigation instructions
        </button>

        <button
          mat-stroked-button
          (click)="gpx()"
          title="Produce a route file that can be used in a gps-device"
          i18n-title="@@plan.output.gpx.tooltip"
          i18n="@@plan.output.gpx">
          GPX file
        </button>

        <button
          mat-stroked-button
          ngxClipboard [cbContent]="planUrl"
          title="Copy a link to this route to the clipboard (for example to keep for later or paste in email)"
          i18n-title="@@plan.output.clipboard.tooltip"
          i18n="@@plan.output.clipboard">
          Copy link to clipboard
        </button>

        <qr-code [value]="planUrl" [size]="200"></qr-code>

      </div>
    </kpn-dialog>
  `,
  styles: [`

    .dialog-content {
      display: flex;
      flex-direction: column;
    }

    .dialog-content > button {
      margin-top: 5px;
      margin-bottom: 5px;
    }

    qr-code {
      margin-top: 25px;
      border: 1px solid black;
      padding: 15px;
    }

  `]
})
export class PlanOutputDialogComponent implements OnInit {

  planUrl = "";

  constructor(private pdfService: PdfService,
              private plannerService: PlannerService) {
  }

  ngOnInit(): void {
    this.planUrl = window.location.href + "#" + PlanUtil.toUrlString(this.plannerService.context.plan);
  }

  printDocument(): void {
    this.pdfService.printDocument(this.plannerService.context.plan);
  }

  printStripDocument(): void {
    this.pdfService.printStripDocument(this.plannerService.context.plan);
  }

  printInstructions(): void {
    this.pdfService.printInstructions(this.plannerService.context.plan);
  }

  gpx(): void {
    new GpxWriter().write(this.plannerService.context.plan);
  }
}
