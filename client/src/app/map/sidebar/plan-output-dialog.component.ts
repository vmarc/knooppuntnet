import {AfterViewInit} from "@angular/core";
import {ElementRef} from "@angular/core";
import {ViewChild} from "@angular/core";
import {OnInit} from "@angular/core";
import {ChangeDetectionStrategy} from "@angular/core";
import {Component} from "@angular/core";
import {Util} from "../../components/shared/util";
import {PdfService} from "../../pdf/pdf.service";
import {GpxWriter} from "../../pdf/plan/gpx-writer";
import {PlannerService} from "../planner.service";
import {PlanUtil} from "../planner/plan/plan-util";
import {Store} from "@ngrx/store";
import {select} from "@ngrx/store";
import {selectPreferencesInstructions} from "../../core/preferences/preferences.selectors";
import {AppState} from "../../core/core.state";

@Component({
  selector: "kpn-plan-output-dialog",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title>
        <div class="kpn-line">
          <mat-icon svgIcon="output"></mat-icon>
          <span i18n="@@plan.output.title">Output</span>
        </div>
      </div>
      <div mat-dialog-content class="dialog-content">

        <mat-form-field>
          <mat-label i18n="@@plan.output.route-name">Route name</mat-label>
          <input
            #routename
            matInput
            placeholder="type route name"
            i18n-placeholder="@@plan.output.route-name-placeholder"
            [value]="name"
            (blur)="nameChanged($event)">
        </mat-form-field>

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
          *ngIf="instructions$ | async"
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
      width: 240px;
    }

    .dialog-content > button {
      margin-top: 5px;
      margin-bottom: 5px;
    }

    qr-code {
      margin-top: 25px;
      border: 1px solid black;
      padding: 10px;
    }

  `]
})
export class PlanOutputDialogComponent implements OnInit, AfterViewInit {

  name = "";
  planUrl = "";
  @ViewChild("routename") input: ElementRef;

  readonly instructions$ = this.store.pipe(select(selectPreferencesInstructions));

  constructor(private pdfService: PdfService,
              private plannerService: PlannerService,
              private store: Store<AppState>) {
  }

  ngOnInit(): void {
    this.name = this.defaultName();
    this.planUrl = this.buildPlanUrl();
  }

  ngAfterViewInit(): void {
    setTimeout(() => this.input.nativeElement.focus(), 250);
  }

  printDocument(): void {
    this.pdfService.printDocument(this.plannerService.context.plan, this.planUrl, this.routeName());
  }

  printStripDocument(): void {
    this.pdfService.printStripDocument(this.plannerService.context.plan, this.routeName());
  }

  printInstructions(): void {
    this.pdfService.printInstructions(this.plannerService.context.plan, this.routeName());
  }

  gpx(): void {
    new GpxWriter().write(this.plannerService.context.plan, this.routeName());
  }

  nameChanged(event): void {
    this.name = event.target.value;
  }

  private routeName(): string {
    if (this.name.length > 0) {
      return this.name;
    }
    return this.defaultName();
  }

  private defaultName(): string {
    const source = this.plannerService.context.plan.sourceNode.nodeName;
    const sink = PlanUtil.planSinkNode(this.plannerService.context.plan).nodeName;
    return Util.today() + " route " + source + " " + sink;
  }

  private buildPlanUrl(): string {
    let root = window.location.href;
    const fragmentIndex = root.indexOf('#');
    if (fragmentIndex > 0) {
      root = root.substr(0, fragmentIndex);
    }
    return root + "#" + PlanUtil.toUrlString(this.plannerService.context.plan);
  }
}
