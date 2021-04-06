import {Input} from '@angular/core';
import {Component} from '@angular/core';
import {MatCheckboxChange} from '@angular/material/checkbox';
import {PoiService} from '../../../services/poi.service';

@Component({
  selector: 'kpn-poi-menu-option',
  // TODO changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-checkbox
      (click)="$event.stopPropagation();"
      [checked]="service.isGroupEnabled(groupName)"
      [disabled]="!service.isEnabled()"
      (change)="enabledChanged($event)"
      class="poi-group">
      <ng-content></ng-content>
    </mat-checkbox>
  `,
  styles: [`
    .poi-group {
      display: block;
      padding-left: 25px;
      padding-right: 10px;
    }
  `]
})
export class PoiMenuOptionComponent {

  @Input() groupName: string;

  constructor(public service: PoiService) {
  }

  enabledChanged(event: MatCheckboxChange): void {
    this.service.updateGroupEnabled(this.groupName, event.checked);
  }

}
