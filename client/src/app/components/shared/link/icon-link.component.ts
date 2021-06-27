import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Reference } from '@api/common/common/reference';

@Component({
  selector: 'kpn-icon-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <kpn-network-type-icon
        [networkType]="reference.networkType"
      ></kpn-network-type-icon>
      <a [routerLink]="url">{{ reference.name }}</a>
      <span *ngIf="mixedNetworkScopes" class="kpn-brackets kpn-thin">
        <kpn-network-scope-name
          [networkScope]="reference.networkScope"
        ></kpn-network-scope-name>
      </span>
    </div>
  `,
})
export class IconLinkComponent {
  @Input() reference: Reference;
  @Input() mixedNetworkScopes: boolean;
  @Input() url: string;
}
