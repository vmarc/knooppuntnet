import { Injectable } from '@angular/core';

@Injectable()
export class WindowService {
  language(): string {
    const lang = window.location.pathname.substring(1, 3);
    if (lang === 'en' || lang === 'de' || lang === 'fr' || lang === 'nl') {
      return lang;
    }
    return '';
  }
}
