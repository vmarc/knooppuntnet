import { Map } from 'immutable';
import { Injectable } from '@angular/core';

@Injectable()
export class I18nService {
  private translations: Map<string, string> = null;

  updateRegistry(elements: HTMLCollection) {
    if (this.translations === null) {
      const keysAndValues: Array<[string, string]> = [];
      Array.from(elements).forEach((span) => {
        const id = span.getAttribute('id');
        const translation = span.textContent;
        keysAndValues.push([id, translation]);
      });
      this.translations = Map<string, string>(keysAndValues);
    }
  }

  isRegistryUpdated() {
    return this.translations !== null;
  }

  translation(key: string): string {
    return this.translations.get(key);
  }
}
