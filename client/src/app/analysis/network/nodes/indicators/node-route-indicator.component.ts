import {ChangeDetectionStrategy} from '@angular/core';
import {OnInit} from '@angular/core';
import {Component, Input} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {NetworkInfoNode} from '../../../../kpn/api/common/network/network-info-node';
import {NodeRouteIndicatorDialogComponent} from './node-route-indicator-dialog.component';

@Component({
  selector: 'kpn-node-route-indicator',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-indicator
      letter="R"
      i18n-letter="@@route-indicator.letter"
      [color]="color"
      (openDialog)="onOpenDialog()">
    </kpn-indicator>
  `
})
export class NodeRouteIndicatorComponent implements OnInit {

  @Input() node: NetworkInfoNode;
  color: string;

  constructor(private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.color = this.node.definedInRoute ? 'green' : 'gray';
  }

  onOpenDialog() {
    this.dialog.open(NodeRouteIndicatorDialogComponent, {data: this.color, maxWidth: 600});
  }
}
