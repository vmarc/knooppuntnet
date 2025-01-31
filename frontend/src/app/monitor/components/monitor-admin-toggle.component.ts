import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { NzSwitchComponent } from 'ng-zorro-antd/switch';
import { MonitorService } from '../monitor.service';

@Component({
  selector: 'kpn-monitor-admin-toggle',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="toggle">
      <nz-switch
        [ngModel]="admin()"
        (ngModelChange)="adminChanged($event)"
        [nzDisabled]="adminDisabled()"
      >
      </nz-switch>
      <span i18n="@@monitor.admin-toggle">Admin</span>
    </div>
  `,
  styles: `
    .toggle {
      display: flex;
      justify-content: flex-end;
      gap: 0.5em;
      align-items: center;
    }
  `,
  imports: [MatSlideToggleModule, NzSwitchComponent, FormsModule],
})
export class MonitorAdminToggleComponent {
  private readonly service = inject(MonitorService);
  readonly admin = this.service.admin;
  readonly adminDisabled = computed(() => this.service.adminRole() === false);

  adminChanged(checked: boolean): void {
    this.service.setAdmin(checked);
  }
}
