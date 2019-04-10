object FrontendCommands {
  val dependencyInstall: String = "yarn install"
  val test: String = "yarn run test-no-watch"
  val serve: String = "yarn run start:nl" // "yarn run start:de"
  val build: String = "yarn run build-prod"
  val buildNL: String = "yarn run build-prod:nl"
  val buildFR: String = "yarn run build-prod:fr"
  val buildDE: String = "yarn run build-prod:de"
}
