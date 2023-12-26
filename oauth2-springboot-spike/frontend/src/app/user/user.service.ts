import { DOCUMENT } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { HttpClient } from '@angular/common/http';
import { OnDestroy } from '@angular/core';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Subscriptions } from '../service/subscriptions';
import { UserStore } from './user.store';

@Injectable()
export class UserService implements OnDestroy {
  private readonly userStore = inject(UserStore);

  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly document = inject(DOCUMENT);

  private readonly subscriptions = new Subscriptions();
  private showDebugInformation = true;
  private returnUrl: string | null = null;

  constructor() {
    this.http.get("/user", {responseType: 'text'}).subscribe(user => {
      this.userStore.updateUser(user);
    })
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  navigateToReturnUrl(): void {
    if (this.returnUrl) {
      const url = this.returnUrl;
      this.returnUrl = null;
      this.router.navigateByUrl(url);
    }
  }

  logout(): void {
    this.http
    .get('/api/logout', {responseType: 'text'}) // erase the cookie
    .subscribe({
      next: () => this.logoutUser(),
      error: (error) => this.logoutError(error),
    });
  }

  private logoutUser(): void {
    if (this.returnUrl) {
      const url = this.returnUrl;
      this.returnUrl = null;
      this.debug(`navigateByUrl(${url})`);
      this.router.navigateByUrl(url);
    }
  }

  private logoutError(error: any): void {
    if (error instanceof HttpErrorResponse) {
      const httpErrorResponse = error as HttpErrorResponse;
      this.userStore.updateError('Could not logout');
      this.userStore.updateErrorDetail(httpErrorResponse.message);
    }
    this.debug('logout() error', error);
  }

  loginLinkClicked(): void {
    this.returnUrl = this.router.url; // preserve url to return to after login
    this.router.navigate(['/user-login']);
  }

  logoutLinkClicked(): void {
    this.returnUrl = this.router.url; // preserve url to return to after logout
    this.router.navigate(['/user-logout']);
  }

  private composeRedirectUri() {
    if (this.document.location.hostname === 'localhost') {
      return `http://127.0.0.1:4000/authenticated`;
    }
    const origin = this.document.location.origin;
    const path = this.document.location.pathname;
    let language = '';
    if (path.length >= 4) {
      if (
        path.startsWith('/en/') ||
        path.startsWith('/nl/') ||
        path.startsWith('/fr/') ||
        path.startsWith('/de/')
      ) {
        language = path.substring(0, 3);
      }
    }
    return `${origin}${language}/authenticated`;
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  private debug(message: string, data?: any): void {
    if (this.showDebugInformation) {
      console.log('UserService: ' + message, data);
    }
  }
}
