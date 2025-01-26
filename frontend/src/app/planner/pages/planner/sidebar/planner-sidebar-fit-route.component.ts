import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { PlannerPageService } from '../planner-page.service';
import { ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'kpn-planner-fit-route',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <button nz-button (click)="zoomToFitRoute()">
        <nz-icon nzType="fullscreen-exit" />
        <span>Zoom to fit entire route</span>
      </button>
    </div>
  `,
  imports: [NzButtonComponent, NzIconDirective],
})
export class PlannerSidebarFitRouteComponent {
  readonly service = inject(PlannerPageService);

  zoomToFitRoute(): void {
    this.service.zoomInToRoute();
  }
}
