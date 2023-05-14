import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { AfterViewInit } from '@angular/core';
import { ElementRef } from '@angular/core';
import { ViewChild } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { Util } from '@app/components/shared';
import { DialogComponent } from '@app/components/shared/dialog';
import { selectPreferencesInstructions } from '@app/core';
import { ApiService } from '@app/services';
import { Store } from '@ngrx/store';
import { ClipboardModule } from 'ngx-clipboard';
import { DirectionsAnalyzer } from '../../../domain/directions/directions-analyzer';
import { PlanUtil } from '../../../domain/plan/plan-util';
import { PdfService } from '../../../pdf/pdf.service';
import { PlannerService } from '../../../planner.service';

@Component({
  selector: 'kpn-plan-output-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-dialog>
      <div mat-dialog-title>
        <div class="kpn-line">
          <mat-icon svgIcon="output" />
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
            (blur)="nameChanged($event)"
          />
        </mat-form-field>

        <button
          mat-stroked-button
          (click)="printTextDocument()"
          title="Produce a route pdf file in 'text' format"
          i18n-title="@@plan.output.text.tooltip"
          i18n="@@plan.output.text-pdf"
        >
          Text
        </button>

        <button
          mat-stroked-button
          (click)="printDocument()"
          title="Produce a route pdf file with compact node overview"
          i18n-title="@@plan.output.compact-pdf.tooltip"
          i18n="@@plan.output.compact-pdf"
        >
          Compact
        </button>

        <button
          mat-stroked-button
          (click)="printStripDocument()"
          title="Produce a route pdf file with nodes in 'strip' format"
          i18n-title="@@plan.output.node-strip-pdf.tooltip"
          i18n="@@plan.output.node-strip-pdf"
        >
          Node strip
        </button>

        <button
          *ngIf="instructions()"
          mat-stroked-button
          (click)="printInstructions()"
          title="Produce a route pdf with navigation instructions"
          i18n-title="@@plan.output.navigation-instructions-pdf.tooltip"
          i18n="@@plan.output.navigation-instructions-pdf"
        >
          Navigation instructions
        </button>

        <button
          mat-stroked-button
          (click)="gpx()"
          title="Produce a route file that can be used in a gps-device"
          i18n-title="@@plan.output.gpx.tooltip"
          i18n="@@plan.output.gpx"
        >
          GPX file
        </button>

        <button
          mat-stroked-button
          ngxClipboard
          [cbContent]="planUrl"
          title="Copy a link to this route to the clipboard (for example to keep for later or paste in email)"
          i18n-title="@@plan.output.clipboard.tooltip"
          i18n="@@plan.output.clipboard"
        >
          Copy link to clipboard
        </button>

        <img [src]="qrCode" alt="qr-code" />
      </div>
    </kpn-dialog>
  `,
  styles: [
    `
      .dialog-content {
        display: flex;
        flex-direction: column;
      }

      .dialog-content > button {
        margin-top: 5px;
        margin-bottom: 5px;
        width: 225px;
      }

      img {
        margin-top: 15px;
        margin-bottom: 15px;
        width: 225px;
        height: 225px;
        border: 1px solid lightgray;
      }
    `,
  ],
  standalone: true,
  imports: [
    DialogComponent,
    MatDialogModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    NgIf,
    ClipboardModule,
    AsyncPipe,
  ],
  providers: [PdfService],
})
export class PlanOutputDialogComponent implements OnInit, AfterViewInit {
  @ViewChild('routename') input: ElementRef;
  name = '';
  planUrl = '';

  qrCode: string | ArrayBuffer = '';

  readonly instructions = this.store.selectSignal(
    selectPreferencesInstructions
  );

  constructor(
    private pdfService: PdfService,
    private plannerService: PlannerService,
    private store: Store,
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.name = this.defaultName();
    this.planUrl = this.buildPlanUrl();
    this.apiService.qrCode(this.planUrl).subscribe((data) => {
      const reader = new FileReader();
      reader.onload = (e) => {
        this.qrCode = e.target.result;
      };
      reader.readAsDataURL(data);
    });
  }

  ngAfterViewInit(): void {
    setTimeout(() => this.input.nativeElement.focus(), 250);
  }

  printDocument(): void {
    this.pdfService.printDocument(
      this.plannerService.context.plan,
      this.planUrl,
      this.routeName(),
      this.qrCode
    );
  }

  printStripDocument(): void {
    this.pdfService.printStripDocument(
      this.plannerService.context.plan,
      this.routeName()
    );
  }

  printTextDocument(): void {
    this.pdfService.printTextDocument(
      this.plannerService.context.plan,
      this.routeName()
    );
  }

  printInstructions(): void {
    const instructions = new DirectionsAnalyzer().analyze(
      this.plannerService.context.plan
    );
    this.pdfService.printInstructions(instructions, this.routeName());
  }

  gpx(): void {
    this.pdfService.writeGpx(
      this.plannerService.context.plan,
      this.routeName()
    );
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
    const sink = PlanUtil.planSinkNode(
      this.plannerService.context.plan
    ).nodeName;
    return Util.today() + ' route ' + source + ' ' + sink;
  }

  private buildPlanUrl(): string {
    let root = window.location.href;
    const fragmentIndex = root.indexOf('?');
    if (fragmentIndex > 0) {
      root = root.substring(0, fragmentIndex);
    }
    return (
      root + '?plan=' + PlanUtil.toUrlString(this.plannerService.context.plan)
    );
  }
}
