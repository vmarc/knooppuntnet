import {ChangeDetectionStrategy} from '@angular/core';
import {Component, Input} from '@angular/core';
import {Store} from '@ngrx/store';
import {actionNodeLink} from '../../../analysis/node/store/node.actions';
import {AppState} from '../../../core/core.state';

@Component({
  selector: 'kpn-link-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [routerLink]="['/analysis/node', nodeId]" (click)="onClick()">{{nodeName}}</a>
  `
})
export class LinkNodeComponent {

  @Input() nodeId: number;
  @Input() nodeName: string;

  constructor(private store: Store<AppState>) {
  }

  onClick(): void {
    this.store.dispatch(actionNodeLink({nodeId: this.nodeId.toString(), nodeName: this.nodeName}));
  }

}
