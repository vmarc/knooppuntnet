package kpn.server.api.monitor.group

import kpn.server.repository.MonitorGroupRepository
import org.springframework.stereotype.Component

@Component
class MonitorGroupNamesBuilder(monitorGroupRepository: MonitorGroupRepository) {

  def build(): Seq[String] = {
    monitorGroupRepository.groups().map(_.name)
  }
}
