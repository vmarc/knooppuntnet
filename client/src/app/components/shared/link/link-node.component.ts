import { ChangeDetectionStrategy } from '@angular/core';
import { Component, Input } from '@angular/core';
import { Store } from '@ngrx/store';

@Component({
  selector: 'kpn-link-node',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <a [routerLink]="['/analysis/node', nodeId]" (click)="onClick()">{{
      nodeName
    }}</a>
  `,
})
export class LinkNodeComponent {
  @Input() nodeId: number;
  @Input() nodeName: string;

  constructor(private store: Store) {}

  onClick(): void {
    // this.store
    //   .dispatch
    // TODO replace with route based mechanism
    // actionNodeLink({
    //   nodeId: this.nodeId.toString(),
    //   nodeName: this.nodeName,
    // })
    // ();
  }
}
