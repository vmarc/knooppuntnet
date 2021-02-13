import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {NetworkType} from '@api/custom/network-type';
import {I18nService} from '../../i18n/i18n.service';

@Component({
  selector: 'kpn-network-type-name',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `{{networkTypeName()}}`
})
export class NetworkTypeNameComponent {

  @Input() networkType: NetworkType;

  constructor(private i18nService: I18nService) {
  }

  networkTypeName(): string {
    return this.i18nService.translation('@@network-type.' + this.networkType);
  }

}
