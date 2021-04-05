import {Injectable} from '@angular/core';

@Injectable()
export class WindowService {

  language(): string {
    const lang = window.location.pathname.substr(1, 2);
    if (lang === 'en' || lang === 'de' || lang === 'fr' || lang === 'nl') {
      return lang;
    }
    return '';
  }

}
