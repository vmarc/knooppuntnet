import { DOCUMENT } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { UserStore } from './user.store';
import * as Sentry from '@sentry/angular-ivy';

@Injectable()
export class UserService {
  private readonly http = inject(HttpClient);
  private readonly userStore = inject(UserStore);
  private readonly document = inject(DOCUMENT);

  constructor() {
    this.http.get('/oauth2/user', { responseType: 'text' }).subscribe((user) => {
      if (user && user.length > 0) {
        this.updateUser(user);
      }
    });
  }

  login(): void {
    this.document.location.assign('/oauth2/authorization/osm');
  }

  logout(): void {
    this.http.post('/oauth2/logout', { responseType: 'text' }).subscribe({
      next: () => this.updateUser(null),
      error: () => this.updateUser(null),
    });
  }

  private updateUser(user: string | null): void {
    if (user) {
      Sentry.setUser({
        _id: user,
      });
    } else {
      Sentry.setUser(null);
    }
    this.userStore.updateUser(user);
  }
}
