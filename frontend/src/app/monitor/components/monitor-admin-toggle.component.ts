import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSlideToggleChange } from '@angular/material/slide-toggle';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MonitorService } from '../monitor.service';

@Component({
  selector: 'kpn-monitor-admin-toggle',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="toggle">
      <mat-slide-toggle
        id="admin-toggle"
        [disabled]="service.adminRole() === false"
        [checked]="service.admin()"
        (change)="adminChanged($event)"
        i18n="@@monitor.admin-toggle"
      >
        Admin
      </mat-slide-toggle>
    </div>
  `,
  styles: `
    .toggle {
      padding-top: 0.5em;
      padding-bottom: 0.5em;
      display: flex;
      justify-content: flex-end;
    }
  `,
  standalone: true,
  imports: [MatSlideToggleModule, AsyncPipe],
})
export class MonitorAdminToggleComponent {
  protected readonly service = inject(MonitorService);

  adminChanged(event: MatSlideToggleChange): void {
    this.service.setAdmin(event.checked);
  }
}
