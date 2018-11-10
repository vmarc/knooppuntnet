import {ChangeDetectorRef, Component, Input} from '@angular/core';
import {UserService} from "../../user.service";
import {PageService} from "../page.service";
import {Subscription} from "rxjs";

@Component({
  selector: 'kpn-page',
  templateUrl: './page.component.html',
  styleUrls: ['./page.component.scss']
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

  isSideNavOpen(): boolean {
    return this.pageService.isSideNavOpen();
  }

}
