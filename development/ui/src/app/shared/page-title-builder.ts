export class PageTitleBuilder {

  private static readonly defaultTitle = "knooppuntnet";

  static setTitle(prefix: string): void {
    document.title = prefix + " | " + this.defaultTitle;
  }

  static setNetworkPageTitle(prefix: string, networkName: string): void {
    document.title = networkName + " | " + prefix + " | " + this.defaultTitle;
  }

}
