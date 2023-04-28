export class ReturnUrl {
  private static readonly questionMarkReplacement = '~';
  private static readonly ampersandReplacement = ',';

  public static fromUrl(url: string): string {
    const pageKey = 'page=';
    const pageKeyStart = url.indexOf(pageKey);
    if (pageKeyStart === -1) {
      const error = 'Could not decode returnUrl';
      const reason = '"page" key not found';
      console.log(`${error} (${reason}): "${url}"`);
      return '/';
    }
    const pageValueStart = pageKeyStart + pageKey.length;
    const pageValueEnd = url.indexOf('&', pageValueStart);
    let pageValue = '';
    if (pageValueEnd === -1) {
      pageValue = url.substring(pageValueStart);
    } else {
      pageValue = url.substring(pageValueStart, pageValueEnd);
    }
    const withQuestionMarksRestored = pageValue.replaceAll(
      this.questionMarkReplacement,
      '?'
    );
    return withQuestionMarksRestored.replaceAll(this.ampersandReplacement, '&');
  }

  public static encode(url: string): string {
    if (
      url.includes(this.questionMarkReplacement) ||
      url.includes(this.ampersandReplacement)
    ) {
      const error = 'Could not encode returnUrl';
      const reason = 'contains "~" and/or "," characters';
      console.log(`${error} (${reason}): "${url}"`);
      return '/';
    }
    const withoutQuestionMarks = url.replaceAll(
      '?',
      this.questionMarkReplacement
    );
    return withoutQuestionMarks.replaceAll('&', this.ampersandReplacement);
  }
}
