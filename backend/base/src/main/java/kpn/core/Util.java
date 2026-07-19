package kpn.core;

public class Util {
  public static boolean isDigits(String text) {
    int len = text.length();
    for (int i = 0; i < len; i++) {
      char c = text.charAt(i);
      if (c < '0' || c > '9') {
        return false;
      }
    }
    return len > 0;
  }
}
