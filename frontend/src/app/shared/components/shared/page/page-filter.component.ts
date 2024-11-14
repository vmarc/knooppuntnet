import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatDrawerContent } from '@angular/material/sidenav';
import { MatDrawer } from '@angular/material/sidenav';
import { MatDrawerContainer } from '@angular/material/sidenav';
import { PageComponent } from './page.component';

@Component({
  selector: 'kpn-page-filter',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-drawer-container class="example-container" hasBackdrop="false">
      <mat-drawer #drawer mode="push">
        <ng-content select="[filter]" />
      </mat-drawer>
      <mat-drawer-content>
        <kpn-page>
          <button mat-raised-button (click)="drawer.toggle()">Filter</button>
          <ng-content />
        </kpn-page>
      </mat-drawer-content>
    </mat-drawer-container>
  `,
  standalone: true,
  imports: [MatDrawerContainer, MatDrawer, MatDrawerContent, PageComponent, MatButton],
})
export class PageFilterComponent {}
