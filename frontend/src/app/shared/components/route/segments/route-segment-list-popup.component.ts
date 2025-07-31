import { output } from '@angular/core';
import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzCheckboxComponent } from 'ng-zorro-antd/checkbox';
import { NzMenuItemComponent } from 'ng-zorro-antd/menu';
import { NzMenuDirective } from 'ng-zorro-antd/menu';

@Component({
  selector: 'ui-route-segment-list-popup',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <ul nz-menu>
      <li nz-menu-item>
        <label
          nz-checkbox
          [nzChecked]="showSegments()"
          (nzCheckedChange)="showSegmentsChanged($event)"
        >
          Show in map
        </label>
      </li>
      <li nz-menu-item (click)="zoomToFitRouteClicked()">Zoom to fit route</li>
      <li nz-menu-item>Option two</li>
    </ul>
  `,
  imports: [NzMenuDirective, NzMenuItemComponent, NzCheckboxComponent],
})
export class RouteSegmentListPopupComponent {
  readonly showSegments = input.required<boolean>();
  readonly showSegmentsChange = output<boolean>();
  readonly zoomToFitRoute = output<void>();

  showSegmentsChanged(value: boolean): void {
    this.showSegmentsChange.emit(value);
  }

  zoomToFitRouteClicked(): void {
    this.zoomToFitRoute.emit();
  }
}
