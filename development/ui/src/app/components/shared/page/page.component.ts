import {ChangeDetectorRef, Component, Input} from '@angular/core';
import {UserService} from "../../../user.service";
import {PageService} from "../page.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'kpn-page',
  template: `
    <div class="page-container">

      <ng-content select="[toolbar]"></ng-content>

      <mat-divider></mat-divider>

      <mat-sidenav-container class="page-sidenav-container">
        <mat-sidenav [mode]="isMobile() ? 'over' : 'side'"
                     [fixedInViewport]="!isMobile()"
                     fixedTopGap="48"
                     [opened]="isSidebarOpen()">
          <ng-content select="[sidebar]"></ng-content>
        </mat-sidenav>

        <mat-sidenav-content>
          <div [ngClass]="{'content-with-margin': !withoutMargin}" style="height: 100%">
            <ng-content select="[content]"></ng-content>
          </div>
        </mat-sidenav-content>
      </mat-sidenav-container>
    </div>
  `,
  styles: [`
    .content-with-margin {
      /*
		The default margin to be used around the page content. This margin should
		not be used for example in pages that do show a map (which should take
		the entire content area).
	  */
      margin: 10px;
    }

    .page-container {
      display: flex;
      flex-direction: column;
      position: absolute;
      top: 0;
      bottom: 0;
      left: 0;
      right: 0;
    }

    .page-is-mobile .page-toolbar {
      position: fixed;
      /* Make sure the toolbar will stay on top of the content as it scrolls past. */
      z-index: 2;
    }

    .page-sidenav-container {
      /*
		When the sidenav is not fixed, stretch the sidenav container to fill
		the available space. This causes \`<mat-sidenav-content>\` to act as our
		scrolling element for desktop layouts.
	  */
      flex: 1;
    }

    .page-is-mobile .page-sidenav-container {
      /*
		When the sidenav is fixed, don't constrain the height of the sidenav container.
		This allows the \`<body>\` to be our scrolling element for mobile layouts.
	  */
      flex: 1 0 auto;
    }

    mat-sidenav {
      min-width: 320px;
    }

    .nav-item-selected {
      background-color: lightgrey;
    }
  `]
})
export class PageComponent {

  @Input() withoutMargin: boolean;

  breakPointStateSubscription: Subscription;

  constructor(changeDetectorRef: ChangeDetectorRef,
              private userService: UserService,
              private pageService: PageService) {
    this.breakPointStateSubscription = this.pageService.breakpointState.subscribe(() => changeDetectorRef.detectChanges());
  }

  ngOnDestroy(): void {
    this.breakPointStateSubscription.unsubscribe();
  }

  isMobile(): boolean {
    return this.pageService.isMobile();
  }

  isSidebarOpen(): boolean {
    return this.pageService.isSidebarOpen();
  }

}
