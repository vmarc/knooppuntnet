import { input } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatLabel } from '@angular/material/form-field';
import { MatRadioButton } from '@angular/material/radio';
import { NetworkType } from '@api/common';
import { NetworkTypeIconComponent } from '@app/components/shared';

@Component({
  selector: 'kpn-configuration-network-type-item',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-radio-button [value]="networkType()">
      <div class="item">
        <kpn-network-type-icon [networkType]="networkType()" />
        <mat-label>{{ label() }}</mat-label>
      </div>
    </mat-radio-button>
  `,
  styles: [
    `
      mat-radio-button {
        display: block;
      }

      .item {
        display: flex;
        align-items: center;
      }

      .item mat-label {
        margin-left: 0.7em;
      }
    `,
  ],
  imports: [MatLabel, MatRadioButton, NetworkTypeIconComponent],
})
export class ConfigurationNetworkTypeItemComponent {
  label = input.required<string>();
  networkType = input.required<NetworkType>();
}
