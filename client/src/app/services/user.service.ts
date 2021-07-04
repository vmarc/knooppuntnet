import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BrowserStorageService } from './browser-storage.service';
import * as Sentry from '@sentry/angular';

@Injectable()
export class UserService {
  loginCallbackPage = '';
  language = '';

  constructor(
    private http: HttpClient,
    private router: Router,
    private browserStorageService: BrowserStorageService
  ) {
    const user = browserStorageService.get('user');
    if (user !== null) {
      Sentry.setUser({ id: user });
    }
  }

  public isLoggedIn(): boolean {
    return this.browserStorageService.get('user') !== null;
  }

  public currentUser(): string {
    const user = this.browserStorageService.get('user');
    if (user !== null) {
      return user;
    }
    return '';
  }

  registerLoginCallbackPage(): void {
    const lang = window.location.pathname.substr(1, 2);
    if (lang === 'en' || lang === 'de' || lang === 'fr' || lang === 'nl') {
      this.language = lang;
    } else {
      this.language = '';
    }
    if (window.location.pathname.endsWith('/login')) {
      this.loginCallbackPage = '/';
    } else {
      if (this.language.length > 0) {
        this.loginCallbackPage = window.location.pathname.substr(3);
      } else {
        this.loginCallbackPage = window.location.pathname;
      }
    }
    console.log(
      'UserService.registerLoginCallbackPage(): registered loginCallbackPage=' +
        this.loginCallbackPage +
        ', based on window.location.pathname=' +
        window.location.pathname +
        ', language=' +
        this.language
    );
  }

  public login(): void {
    const languageUrlPart = this.language.length > 0 ? '/' + this.language : '';
    const loginUrl =
      '/api/login?callbackUrl=' +
      window.location.origin +
      languageUrlPart +
      '/authenticate?page=' +
      this.loginCallbackPage;

    console.log('DEBUG UserService login loginUrl=' + loginUrl);

    // TODO scala version has timeout = 25000

    this.http
      .get(loginUrl, {
        responseType: 'text',
      })
      .subscribe(
        (r) => {
          console.log('DEBUG UserService success');
          console.log(JSON.stringify(r, null, 2));
          window.location.href =
            'https://www.openstreetmap.org/oauth/authorize?oauth_token=' + r;
        },
        (error) => {
          console.log('DEBUG UserService error response');
          console.log(JSON.stringify(error, null, 2));
        },
        () => {
          console.log('DEBUG UserService complete');
        }
      );
  }

  public logout(): void {
    this.http
      .get('/api/logout', {
        responseType: 'text',
      })
      .subscribe(
        (r) => {
          console.log('DEBUG logout success');
          console.log(JSON.stringify(r, null, 2));
          this.browserStorageService.remove('user');
          Sentry.setUser(null);
        },
        (error) => {
          console.log('DEBUG logout error response');
          console.log(JSON.stringify(error, null, 2));
        },
        () => {
          console.log('DEBUG logout complete');
        }
      );
  }

  authenticated() {
    const search = decodeURIComponent(window.location.search);
    this.http
      .get('/api/authenticated' + search, {
        responseType: 'text',
      })
      .subscribe(
        (user) => {
          this.browserStorageService.set('user', user);
          Sentry.setUser({ id: user });
          console.log('DEBUG authenticated success, user=' + user);
          console.log('DEBUG search=' + search);

          const withoutQuestionMark = search.substr(1);
          const firstParam = withoutQuestionMark.split('&')[0];
          const keyAndValue = firstParam.split('=');
          const page = keyAndValue[1].substring(1);
          const pageArray = page.split('/');

          console.log('DEBUG pageArray=' + JSON.stringify(pageArray, null, 2));

          this.router.navigate(pageArray);
        },
        (error) => {
          console.log('DEBUG authenticated error response');
          console.log(JSON.stringify(error, null, 2));
        },
        () => {
          console.log('DEBUG authenticated complete');
        }
      );
  }

  private parseJwt(token: string) {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map(function (c) {
          return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        })
        .join('')
    );

    return JSON.parse(jsonPayload);
  }
}
