import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatExpansionPanelHeader } from '@angular/material/expansion';
import { MatExpansionPanelContent } from '@angular/material/expansion';
import { MatExpansionPanel } from '@angular/material/expansion';
import { MatRadioChange } from '@angular/material/radio';
import { MatRadioGroup } from '@angular/material/radio';
import { NetworkType } from '@api/custom';
import { IconButtonComponent } from '@app/components/shared/icon';
import { RootService } from '../root.service';
import { ConfigurationNetworkTypeItemComponent } from './configuration-network-type-item.component';

@Component({
  selector: 'kpn-configuration-network-type',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-expansion-panel [expanded]="expanded()" (expandedChange)="expandedChanged($event)">
      <mat-expansion-panel-header> Route type</mat-expansion-panel-header>
      <ng-template matExpansionPanelContent>
        <mat-radio-group [value]="selectedNetworkType()" (change)="networkTypeChanged($event)">
          <kpn-configuration-network-type-item
            [networkType]="networkType.hiking"
            i18n-label="@@network-type.hiking"
            label="Hiking"
          />
          <kpn-configuration-network-type-item
            [networkType]="networkType.cycling"
            i18n-label="@@network-type.cycling"
            label="Cycling"
          />
          <kpn-configuration-network-type-item
            [networkType]="networkType.horseRiding"
            i18n-label="@@network-type.horse-riding"
            label="Horse riding"
          />
          <kpn-configuration-network-type-item
            [networkType]="networkType.motorboat"
            i18n-label="@@network-type.motorboat"
            label="Motorboat"
          />
          <kpn-configuration-network-type-item
            [networkType]="networkType.canoe"
            i18n-label="@@network-type.canoe"
            label="Canoe"
          />
          <kpn-configuration-network-type-item
            [networkType]="networkType.inlineSkating"
            i18n-label="@@network-type.inlineSkating"
            label="Inline skating"
          />
        </mat-radio-group>
      </ng-template>
    </mat-expansion-panel>
  `,
  standalone: true,
  imports: [
    MatRadioGroup,
    ConfigurationNetworkTypeItemComponent,
    IconButtonComponent,
    MatExpansionPanel,
    MatExpansionPanelContent,
    MatExpansionPanelHeader,
  ],
})
export class ConfigurationNetworkTypeComponent {
  private readonly rootService = inject(RootService);
  protected readonly networkType = NetworkType;
  protected readonly selectedNetworkType = this.rootService.networkType;

  networkTypeChanged(event: MatRadioChange) {
    this.rootService.updateNetworkType(event.value);
  }

  expanded(): boolean {
    return true;
  }

  expandedChanged(value: boolean): void {}
}
