export class MonitorTranslations {
  private static readonly translations = new Map<string, string>([
    ['monitor', $localize`:@@monitor:Monitor`],
  ]);

  public static get(key: string): string {
    return this.translations.get(key);
  }
}
