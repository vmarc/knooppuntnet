import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {Reference} from '@api/common/common/reference';

@Component({
  selector: 'kpn-icon-network-link',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-icon-link [reference]="reference" [url]="'/analysis/network/' + reference.id"></kpn-icon-link>
  `
})
export class IconNetworkLinkComponent {
  @Input() reference: Reference;
}
