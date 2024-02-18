import { Map as TranslationMap } from 'immutable';

export class MonitorTranslations {
  private static readonly translations: TranslationMap<string, string> = this.buildTranslations();

  public static translate(key: string): string {
    return this.translations.get(key);
  }

  private static buildTranslations(): TranslationMap<string, string> {
    const keysAndValues: Array<[string, string]> = [];
    keysAndValues.push(['monitor', $localize`:@@monitor:Monitor`]);
    return TranslationMap<string, string>(keysAndValues);
  }
}
