import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatLabel } from '@angular/material/form-field';
import { BackButtonComponent } from '@app/components/shared';
import { PageButtonsComponent } from '@app/components/shared/page';
import { ConfigurationLayersComponent } from './configuration-layers.component';
import { ConfigurationModeComponent } from './configuration-mode.component';
import { ConfigurationNetworkTypeComponent } from './configuration-network-type.component';

@Component({
  selector: 'kpn-configuration',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-buttons>
      <kpn-back-button />
      <mat-label>Configuration</mat-label>
    </kpn-page-buttons>
    <div class="panels">
      <kpn-configuration-network-type />
      <kpn-configuration-mode />
      <kpn-configuration-layers />
    </div>
  `,
  styles: `
    .panels {
      display: flex;
      flex-direction: column;
      gap: 8px;
      margin: 8px;
    }
  `,
  standalone: true,
  imports: [
    BackButtonComponent,
    ConfigurationNetworkTypeComponent,
    ConfigurationLayersComponent,
    ConfigurationModeComponent,
    MatLabel,
    PageButtonsComponent,
  ],
})
export class ConfigurationComponent {}
