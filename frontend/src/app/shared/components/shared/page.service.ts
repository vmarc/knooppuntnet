import { computed } from '@angular/core';
import { signal } from '@angular/core';
import { effect } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { BrowserStorageService } from '@app/services';
import { PageWidthService } from './page-width.service';

@Injectable({
  providedIn: 'root',
})
export class PageService {
  private readonly pageWidthService = inject(PageWidthService);
  private readonly titleService = inject(Title);
  private readonly browserStorageService = inject(BrowserStorageService);

  private readonly defaultTitle = 'knooppuntnet';
  private readonly sideBarOpenLocalStorageKey = 'sidebar-open';

  private readonly _toolbarBackgroundColor = signal<string>(null);
  private readonly _manualSidebarOpen = signal<boolean | null>(null);

  private initializing = true;

  readonly toolbarBackgroundColor = this._toolbarBackgroundColor.asReadonly();
  readonly sidebarOpen = computed(() => {
    const manualSidebarOpen = this._manualSidebarOpen();
    const large = !this.pageWidthService.isAllSmall();
    if (this.initializing) {
      this.initializing = false;
      const lastKnownSidebarOpen = this.browserStorageService.get(this.sideBarOpenLocalStorageKey);
      if (lastKnownSidebarOpen === null) {
        return large;
      }
      return lastKnownSidebarOpen === 'true';
    }
    if (manualSidebarOpen !== null) {
      return manualSidebarOpen;
    }
    return large;
  });

  constructor() {
    effect(() => {
      this.rememberSidebarOpen(this.sidebarOpen());
    });
  }

  toggleSidebarOpen(): void {
    this._manualSidebarOpen.set(!this.sidebarOpen());
  }

  setTitle(prefix: string): void {
    const title = prefix ? prefix + ' | ' + this.defaultTitle : this.defaultTitle;
    this.titleService.setTitle(title);
  }

  setToolbarBackgroundColor(color: string): void {
    this._toolbarBackgroundColor.set(color);
  }

  private rememberSidebarOpen(sidebarOpen: boolean): void {
    const sidebarOpenOpenString = sidebarOpen === true ? 'true' : 'false';
    this.browserStorageService.set(this.sideBarOpenLocalStorageKey, sidebarOpenOpenString);
  }
}
