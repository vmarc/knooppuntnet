import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { NzButtonComponent } from 'ng-zorro-antd/button';
import { NzCollapsePanelComponent } from 'ng-zorro-antd/collapse';
import { NzCollapseComponent } from 'ng-zorro-antd/collapse';
import { NzDrawerContentDirective } from 'ng-zorro-antd/drawer';
import { NzDrawerComponent } from 'ng-zorro-antd/drawer';
import { NzIconDirective } from 'ng-zorro-antd/icon';
import { SettingsMenuLayersComponent } from './settings-menu-layers.component';
import { SettingsMenuMapOptionsComponent } from './settings-menu-map-options.component';
import { SettingsMenuScopeComponent } from './settings-menu-scope.component';

@Component({
  selector: 'kpn-settings-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button nz-button (click)="open()">
      <nz-icon nzType="setting" />
    </button>

    <nz-drawer
      [nzClosable]="true"
      [nzVisible]="visible"
      [nzPlacement]="'left'"
      nzTitle="Settings"
      (nzOnClose)="close()"
    >
      <ng-container *nzDrawerContent>
        <nz-collapse nzAccordion>
          <nz-collapse-panel nzHeader="Map options" nzActive="true">
            <kpn-settings-menu-map-options />
          </nz-collapse-panel>
          <nz-collapse-panel nzHeader="Scope" nzActive="true">
            <kpn-settings-menu-scope />
          </nz-collapse-panel>
          <nz-collapse-panel nzHeader="Layers" nzActive="true">
            <kpn-settings-menu-layers />
          </nz-collapse-panel>
        </nz-collapse>
      </ng-container>
    </nz-drawer>
  `,
  imports: [
    NzButtonComponent,
    NzCollapseComponent,
    NzCollapsePanelComponent,
    NzDrawerComponent,
    NzDrawerContentDirective,
    NzIconDirective,
    SettingsMenuLayersComponent,
    SettingsMenuMapOptionsComponent,
    SettingsMenuScopeComponent,
  ],
})
export class SettingsMenuComponent {
  visible = false;

  open(): void {
    this.visible = true;
  }

  close(): void {
    this.visible = false;
  }
}
