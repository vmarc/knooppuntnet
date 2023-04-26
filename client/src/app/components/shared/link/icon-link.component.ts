import { OnInit } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Reference } from '@api/common/common';

@Component({
  selector: 'kpn-icon-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="kpn-line">
      <kpn-network-type-icon [networkType]="reference.networkType" />
      <a [routerLink]="link" [state]="state" title="">{{ reference.name }}</a>
      <span *ngIf="mixedNetworkScopes" class="kpn-brackets kpn-thin">
        <kpn-network-scope-name [networkScope]="reference.networkScope" />
      </span>
    </div>
  `,
})
export class IconLinkComponent implements OnInit {
  @Input() reference: Reference;
  @Input() mixedNetworkScopes: boolean;
  @Input() elementType: string;
  protected state = {};
  protected link: string;

  ngOnInit() {
    this.link = `/analysis/${this.elementType}/${this.reference.id}`;
    this.state['networkType'] = this.reference.networkType;
    this.state[this.elementType + 'Name'] = this.reference.name;
  }
}
