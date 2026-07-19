package kpn.server.monitor.group

import kpn.server.monitor.repository.MonitorGroupRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class MonitorGroupNamesBuilder(monitorGroupRepository: MonitorGroupRepository) {

  def build(): Seq[String] = {
    monitorGroupRepository.groups().map(_.name)
  }
}
