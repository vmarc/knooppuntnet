import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { FormGroupDirective } from '@angular/forms';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'kpn-monitor-route-properties-step-1-name',
  template: `
    <kpn-monitor-route-name [ngForm]="ngForm" [name]="name">
    </kpn-monitor-route-name>
    <kpn-monitor-route-description
      [ngForm]="ngForm"
      [description]="description"
    ></kpn-monitor-route-description>
    <div class="kpn-button-group">
      <button mat-stroked-button matStepperNext>Next</button>
    </div>
  `,
})
export class MonitorRoutePropertiesStep1NameComponent {
  @Input() ngForm: FormGroupDirective;
  @Input() name: FormControl<string>;
  @Input() description: FormControl<string>;
}
