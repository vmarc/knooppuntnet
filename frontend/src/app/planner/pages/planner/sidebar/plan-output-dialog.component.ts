import { DOCUMENT } from '@angular/core';
import { viewChild } from '@angular/core';
import { inject } from '@angular/core';
import { AfterViewInit } from '@angular/core';
import { ElementRef } from '@angular/core';
import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { PreferencesService } from '@app/shared/core/preferences/preferences.service';
import { ApiService } from '@app/shared/services/api.service';
import { DialogComponent } from '@app/shared/components/dialog/dialog.component';
import { Util } from '@app/shared/components/util';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { ClipboardModule } from 'ngx-clipboard';
import { PlanUtil } from '../../../domain/plan/plan-util';
import { PdfService } from '../../../pdf/pdf.service';
import { PlannerService } from '../planner.service';

@Component({
  selector: 'ui-plan-output-dialog',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ui-dialog>
      <div dialog-title>
        <div class="kpn-line">
          <nz-icon nzType="export" />
          <span i18n="@@plan.output.title">Output</span>
        </div>
      </div>
      <div class="dialog-content">
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
          nz-button
          (click)="printTextDocument()"
          title="Produce a route pdf file in 'text' format"
          i18n-title="@@plan.output.text.tooltip"
          i18n="@@plan.output.text-pdf"
        >
          Text
        </button>

        <button
          nz-button
          (click)="printDocument()"
          title="Produce a route pdf file with compact node overview"
          i18n-title="@@plan.output.compact-pdf.tooltip"
          i18n="@@plan.output.compact-pdf"
        >
          Compact
        </button>

        <button
          nz-button
          (click)="printStripDocument()"
          title="Produce a route pdf file with nodes in 'strip' format"
          i18n-title="@@plan.output.node-strip-pdf.tooltip"
          i18n="@@plan.output.node-strip-pdf"
        >
          Node strip
        </button>

        <button
          nz-button
          (click)="gpx()"
          title="Produce a route file that can be used in a gps-device"
          i18n-title="@@plan.output.gpx.tooltip"
          i18n="@@plan.output.gpx"
        >
          GPX file
        </button>

        <button
          nz-button
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
    </ui-dialog>
  `,
  styles: `
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
  providers: [PdfService],
  imports: [
    ClipboardModule,
    DialogComponent,
    MatFormFieldModule,
    MatInputModule,
    NzButtonComponent,
    NzIconDirective,
  ],
})
export class PlanOutputDialogComponent implements OnInit, AfterViewInit {
  private readonly input = viewChild<ElementRef>('routename');

  private readonly pdfService = inject(PdfService);
  private readonly plannerService = inject(PlannerService);
  private readonly apiService = inject(ApiService);
  private readonly preferencesService = inject(PreferencesService);
  private readonly document = inject(DOCUMENT);
  private readonly window = this.document?.defaultView;

  protected name = '';
  protected planUrl = '';

  protected qrCode: string | ArrayBuffer = '';

  private readonly plan = this.plannerService.context.plan;

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
    setTimeout(() => this.input().nativeElement.focus(), 250);
  }

  printDocument(): void {
    this.pdfService.printDocument(this.plan(), this.planUrl, this.routeName(), this.qrCode);
  }

  printStripDocument(): void {
    this.pdfService.printStripDocument(this.plan(), this.routeName());
  }

  printTextDocument(): void {
    this.pdfService.printTextDocument(this.plan(), this.routeName());
  }

  gpx(): void {
    this.pdfService.writeGpx(this.plan(), this.routeName());
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
    const source = this.plan().sourceNode.nodeName;
    const sink = PlanUtil.planSinkNode(this.plan()).nodeName;
    return Util.today() + ' route ' + source + ' ' + sink;
  }

  private buildPlanUrl(): string {
    let root = this.window.location.href;
    const fragmentIndex = root.indexOf('?');
    if (fragmentIndex > 0) {
      root = root.substring(0, fragmentIndex);
    }
    return root + '?plan=' + PlanUtil.toUrlString(this.plan());
  }
}
