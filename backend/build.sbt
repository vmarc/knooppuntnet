scalaVersion := "2.13.18"

val springBootVersion = "3.2.1"
val springVersion = "6.1.2"
val springSecVersion = "6.2.1"
val micrometerVersion = "1.12.1"
val mongodbVersion = "5.6.2"
val geoToolsVersion = "32.0"

lazy val api = project
lazy val web = project
lazy val core = project
lazy val analysis = project

lazy val root = rootProject
  .dependsOn(api, web, core, analysis)
  .settings(
    name := "backend",
    organization := "knooppuntnet",
    version := "5.0.16",

    javacOptions ++= Seq("--release", "17"),
    scalacOptions ++= Seq(
      "-deprecation",
      "-Xsource:3",
    ),

    libraryDependencies ++= Seq(

      // Spring Boot starters
      ("org.springframework.boot" % "spring-boot-starter" % springBootVersion),
      ("org.springframework.boot" % "spring-boot-starter-web" % springBootVersion),
      ("org.springframework.boot" % "spring-boot-starter-actuator" % springBootVersion),
      ("org.springframework.boot" % "spring-boot-starter-mail" % springBootVersion),
      "org.springframework.boot" % "spring-boot-starter-webflux" % springBootVersion,
      "org.springframework.boot" % "spring-boot-starter-oauth2-client" % springBootVersion,
      ("org.springframework.boot" % "spring-boot-starter-log4j2" % springBootVersion),
      "org.springframework.boot" % "spring-boot-devtools" % springBootVersion % Optional,

      // Spring Framework
      "org.springframework" % "spring-websocket" % springVersion,
      "org.springframework" % "spring-messaging" % springVersion,

      // Spring Security (version managed by Spring Boot BOM)
      "org.springframework.security" % "spring-security-config" % springSecVersion,
      "org.springframework.security" % "spring-security-web" % springSecVersion,
      "org.springframework.security" % "spring-security-crypto" % springSecVersion,

      // Spring Social
      "org.springframework.social" % "spring-social-core" % "1.1.6.RELEASE",

      // Micrometer (version managed by Spring Boot BOM)
      "io.micrometer" % "micrometer-core" % micrometerVersion,
      "io.micrometer" % "micrometer-registry-prometheus" % micrometerVersion,

      // JWT
      "com.nimbusds" % "nimbus-jose-jwt" % "9.40",

      // Scala modules
      "org.scala-lang.modules" %% "scala-xml" % "2.3.0",
      "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4",

      // Enumeratum
      "com.beachape" %% "enumeratum" % "1.7.3",

      // Apache Commons
      "commons-lang" % "commons-lang" % "2.6",
      "org.apache.commons" % "commons-text" % "1.11.0",
      "commons-io" % "commons-io" % "2.16.1",
      "commons-net" % "commons-net" % "3.11.0",
      "commons-codec" % "commons-codec" % "1.16.0",

      // JTS / GeoTools
      "org.locationtech.jts" % "jts-core" % "1.20.0",
      "org.locationtech.jts.io" % "jts-io-common" % "1.20.0",
      ("org.geotools" % "gt-api" % geoToolsVersion)
        .exclude("javax.media", "jai_core"),
      ("org.geotools" % "gt-referencing" % geoToolsVersion)
        .exclude("javax.media", "jai_core"),
      ("org.geotools" % "gt-geojson-store" % geoToolsVersion)
        .exclude("javax.media", "jai_core").classifier("sources"),
      ("org.geotools" % "gt-epsg-hsql" % geoToolsVersion)
        .exclude("javax.media", "jai_core"),

      // GeoPackage
      ("mil.nga.geopackage" % "geopackage" % "6.6.5")
        .exclude("org.slf4j", "slf4j-nop"),

      // Graph
      "org.jgrapht" % "jgrapht-core" % "1.5.2",

      // CLI / utilities
      "com.github.scopt" %% "scopt" % "4.1.0",
      "jline" % "jline" % "2.14.6",
      "it.unimi.dsi" % "fastutil" % "8.5.18",

      // Protobuf
      "com.google.protobuf.nano" % "protobuf-javanano" % "3.1.0",

      // Image processing
      ("com.sksamuel.scrimage" % "scrimage-core" % "4.1.3")
        .exclude("ch.qos.logback", "logback-core")
        .exclude("ch.qos.logback", "logback-classic")
        .exclude("org.jetbrains.kotlin", "kotlin-stdlib-jdk8"),

      // QR codes
      "com.google.zxing" % "core" % "3.5.3",

      // MongoDB
      "org.mongodb" % "mongodb-driver-sync" % mongodbVersion,

      // Domain-specific
      "ch.poole" % "OpeningHoursParser" % "0.28.2",
      "nl.basjes.parse.useragent" % "yauaa" % "7.26.1",
      "no.ecc.vectortile" % "java-vector-tile" % "1.3.23",
      "de.sstoehr" % "har-reader" % "2.3.0",
      "javax.annotation" % "javax.annotation-api" % "1.3.2",
      "com.github.mwiede" % "jsch" % "0.2.18",

      // Test dependencies
      "org.springframework.boot" % "spring-boot-starter-test" % springBootVersion % Test,
      "org.scalatest" %% "scalatest" % "3.2.19" % Test,
      "org.scalamock" %% "scalamock" % "7.5.2" % Test,
      "com.microsoft.playwright" % "playwright" % "1.44.0" % Test,
    ),
    excludeDependencies ++= Seq(
      ExclusionRule("org.springframework.boot", "spring-boot-starter-logging")
    ),
    resolvers ++= Seq(
      "OSGeo Release" at "https://repo.osgeo.org/repository/release/",
      "OSGeo Snapshot" at "https://repo.osgeo.org/repository/snapshot/",
      "ECC" at "https://maven.ecc.no/releases",
    ),

    // Assembly (fat JAR) settings
    assembly / mainClass := Some("kpn.server.ServerApplication"),
    assembly / assemblyJarName := "server.jar",
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", "spring.handlers") => MergeStrategy.concat
      case PathList("META-INF", "spring.schemas") => MergeStrategy.concat
      case PathList("META-INF", "spring.factories") => MergeStrategy.concat
      case PathList("META-INF", "spring", _*) => MergeStrategy.last
      case PathList("META-INF", "services", _*) => MergeStrategy.filterDistinctLines
      case PathList("META-INF", _*) => MergeStrategy.discard
      case "module-info.class" => MergeStrategy.discard
      case x => MergeStrategy.first
    },
  )
