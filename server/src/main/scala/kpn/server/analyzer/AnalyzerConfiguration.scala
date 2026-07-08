package kpn.server.analyzer

import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorLocalImpl
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.tools.config.Dirs
import kpn.core.tools.status.StatusRepository
import kpn.server.analyzer.engine.changes.OsmChangeRepository
import kpn.server.analyzer.engine.changes.OsmChangeRepositoryImpl
import kpn.server.analyzer.engine.changes.changes.ChangeSetInfoApi
import kpn.server.analyzer.engine.changes.changes.ChangeSetInfoApiImpl
import kpn.server.analyzer.engine.context.AnalysisContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AnalyzerConfiguration {

  @Bean
  def dirs: Dirs = Dirs()

  @Bean
  def statusRepository: StatusRepository = new StatusRepository(dirs)

  @Bean
  def overpassQueryExecutor(
    @Value("${app.overpass.remote:false}") remote: Boolean,
    @Value("${app.overpass.remote.url:https://overpass-api.de/api/interpreter}") url: String
  ): OverpassQueryExecutor = {
    if (remote) {
      new OverpassQueryExecutorRemoteImpl(url)
    }
    else {
      new OverpassQueryExecutorLocalImpl()
    }
  }

  @Bean
  def osmChangeRepository: OsmChangeRepository = {
    new OsmChangeRepositoryImpl(dirs.replicate)
  }

  @Bean
  def changeSetInfoApi: ChangeSetInfoApi = {
    new ChangeSetInfoApiImpl(dirs.changeSets)
  }

  @Bean
  def analysisContext: AnalysisContext = {
    new AnalysisContext()
  }
}
