import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'kpn-monitor-route-properties-step-2-name',
  template: `
    <kpn-monitor-route-name [ngForm]="ngForm" [name]="name">
    </kpn-monitor-route-name>
    <kpn-monitor-route-description
      [ngForm]="ngForm"
      [description]="description"
    ></kpn-monitor-route-description>
    <div class="kpn-button-group">
      <button
        *ngIf="mode === 'update'"
        mat-stroked-button
        matStepperPrevious
        i18n="@@action.back"
      >
        Back
      </button>
      <button mat-stroked-button matStepperNext i18n="@@action.next">
        Next
      </button>
    </div>
  `,
})
export class MonitorRoutePropertiesStep2NameComponent {
  @Input() mode: string;
  @Input() ngForm: FormGroupDirective;
  @Input() name: FormControl<string>;
  @Input() description: FormControl<string>;
}
