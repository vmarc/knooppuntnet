package kpn.core.tools.log.analyzers

import kpn.core.tools.log.LogAnalysisContext
import kpn.core.tools.log.LogRecord

object RobotAnalyzer extends LogRecordAnalyzer {

  def analyze(record: LogRecord, context: LogAnalysisContext): LogAnalysisContext = {
    if (isRobot(record)) {
      context.withValue("robot").copy(recordAnalysis = context.recordAnalysis.copy(robot = true))
    }
    else {
      context.withValue("non-robot")
    }
  }

  private def isRobot(record: LogRecord): Boolean = {

    if (record.userAgent != null) {
      robotSignatures.exists(record.userAgent.contains)
    }
    else {
      false
    }
  }

  private val robotSignatures = Seq(
    "Mozilla/5.0 QGIS/31600", // not really a robot? but 387076 tile requests, 15 to 19 nov 2020
    "Adsbot",
    "adscanner",
    "adstxt.com",
    "AhrefsBot",
    "Applebot",
    "archive.org_bot",
    "AspiegelBot",
    "AspiegelBot",
    "Baiduspider",
    "BarkRowler",
    "Barkrowler",
    "bbot",
    "bingbot",
    "bingbot",
    "BingPreview",
    "BLEXBot",
    "BorneoBot",
    "brands-bot-logo",
    "CCBot",
    "CheckMarkNetwork",
    "Clarabot",
    "Cliqzbot",
    "CMS Crawler",
    "crawler4j",
    "Dataprovider.com",
    "domaincrawler",
    "DomainCrawler",
    "Domains Project",
    "DomainStatsBot",
    "DotBot",
    "DuckDuckGo",
    "DYNO Mapper Crawler",
    "Elisabot",
    "e.ventures Investment Crawler",
    "Facebot",
    "FatBoyKimComBot",
    "FeedFetcher-Google",
    "filibot",
    "GarlikCrawler",
    "Gather Analyze Provide",
    "Gluten Free Crawler",
    "GoogIebot",
    "Googlebot",
    "Google Page Speed Insights",
    "Google-Site-Verification",
    "Grobbot",
    "Gulper Web Bot",
    "HealthCheckBot",
    "IndeedBot",
    "Internet-structure-research-project-bot",
    "JobboerseBot",
    "KomodiaBot",
    "Let's Encrypt validation server",
    "MauiBot",
    "MegaIndex",
    "MJ12bot",
    "msnbot",
    "NetcraftSurveyAgent",
    "netEstate NE Crawler",
    "Nimbostratus-Bot",
    "oBot",
    "PetalBot",
    "Qwantbot",
    "Qwantify",
    "RoxieyBot",
    "RU_Bot",
    "SafeDNSBot",
    "Scrapy",
    "Screaming Frog SEO Spider",
    "Seekport Crawler",
    "Seekport Crawler",
    "SemanticScholarBot",
    "SemrushBot",
    "SemrushBot",
    "sentry.io",
    "SEOkicks",
    "serpstatbot",
    "SeznamBot",
    "Slackbot",
    "SMTBot",
    "Sogou web spider",
    "SpiderLing",
    "SSL Labs",
    "TelegramBot",
    "TinEye-bot",
    "tomnomnom/meg",
    "trendictionbot",
    "Turnitin",
    "Twingly",
    "Twitterbot",
    "ubermetrics-technologies",
    "Uptimebot",
    "Wappalyzer",
    "webtechbot",
    "woorankreview",
    "XYZ Spider",
    "yacybot",
    "Yahoo! Slurp",
    "YaK",
    "Yandex",
    "YisouSpider",
    "zgrab",
  )

}
