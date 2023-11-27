import { Injectable } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { BrowserStorageService } from '@app/services';
import { Observable } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { PageWidthService } from './page-width.service';

@Injectable({
  providedIn: 'root',
})
export class PageService {
  private readonly sideBarOpenLocalStorageKey = 'sidebar-open';
  readonly toolbarBackgroundColor$: Observable<string>;
  readonly defaultTitle = 'knooppuntnet';
  readonly sidebarOpen: BehaviorSubject<boolean> = new BehaviorSubject(
    this.sidebarOpenInitialState()
  );
  private _toolbarBackgroundColor$: BehaviorSubject<string>;

  private initializing = true;

  constructor(
    private pageWidthService: PageWidthService,
    private titleService: Title,
    private browserStorageService: BrowserStorageService
  ) {
    pageWidthService.current$.subscribe(() => this.pageWidthChanged());
    this._toolbarBackgroundColor$ = new BehaviorSubject<string>(null);
    this.toolbarBackgroundColor$ = this._toolbarBackgroundColor$.asObservable();
    this.initializing = false;
  }

  toggleSidebarOpen(): void {
    const sidebarOpen = !this.sidebarOpen.value;
    this.sidebarOpen.next(sidebarOpen);
    this.remember(sidebarOpen);
  }

  isSidebarOpen(): boolean {
    return this.sidebarOpen.value;
  }

  setTitle(prefix: string): void {
    const title = prefix ? prefix + ' | ' + this.defaultTitle : this.defaultTitle;
    this.titleService.setTitle(title);
  }

  nextToolbarBackgroundColor(color: string): void {
    this._toolbarBackgroundColor$.next(color);
  }

  private pageWidthChanged(): void {
    if (this.initializing === false) {
      const sidebarOpen = this.sidebarOpenBasedOnPageWidth();
      if (this.sidebarOpen.value !== sidebarOpen) {
        this.sidebarOpen.next(sidebarOpen);
        this.remember(sidebarOpen);
      }
    }
  }

  private sidebarOpenInitialState(): boolean {
    const lastKnownSidebarOpen = this.browserStorageService.get(this.sideBarOpenLocalStorageKey);
    if (lastKnownSidebarOpen === null) {
      return this.sidebarOpenBasedOnPageWidth();
    }
    return lastKnownSidebarOpen === 'true';
  }

  private sidebarOpenBasedOnPageWidth(): boolean {
    return !(
      this.pageWidthService.isSmall() ||
      this.pageWidthService.isVerySmall() ||
      this.pageWidthService.isVeryVerySmall()
    );
  }

  private remember(sidebarOpen: boolean): void {
    const sidebarOpenOpenString = sidebarOpen === true ? 'true' : 'false';
    this.browserStorageService.set(this.sideBarOpenLocalStorageKey, sidebarOpenOpenString);
  }
}
