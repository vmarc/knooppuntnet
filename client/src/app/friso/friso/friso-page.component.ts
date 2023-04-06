import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { FrisoNode } from '@app/components/ol/components/friso-node';
import { FrisoNodeDialogComponent } from '@app/friso/friso/friso-node-dialog.component';

@Component({
  selector: 'kpn-friso-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- English only-->
    <!-- eslint-disable @angular-eslint/template/i18n -->
    <ul class="breadcrumb">
      <li><a routerLink="/" i18n="@@breadcrumb.home">Home</a></li>
      <li><span>Routedatabank</span></li>
    </ul>

    <kpn-page-header pageTitle="Routedatabank">
      <span class="header-network-type-icon">
        <mat-icon svgIcon="hiking" />
      </span>
      <span> Netherlands: OSM versus routedatabank comparison </span>
    </kpn-page-header>

    <kpn-friso-map (nodeClicked)="nodeClicked($event)" />
  `,
})
export class FrisoPageComponent {
  constructor(private dialog: MatDialog) {}

  nodeClicked(node: FrisoNode): void {
    this.dialog.open(FrisoNodeDialogComponent, {
      data: node,
      autoFocus: false,
      maxWidth: 600,
    });
  }
}
