package kpn.server.analyzer.engine.analysis.location

object LocationConfigurationTest {

  private var locationConfigurationOption: Option[LocationConfiguration] = None

  def locationConfiguration: LocationConfiguration = {
    if (locationConfigurationOption.isEmpty) {
      val locationConfiguration = new LocationConfigurationReader().read()
      locationConfigurationOption = Some(locationConfiguration)
    }
    locationConfigurationOption.get
  }
}
