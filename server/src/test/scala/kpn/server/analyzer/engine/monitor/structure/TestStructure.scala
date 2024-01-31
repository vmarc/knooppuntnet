package kpn.server.analyzer.engine.monitor.structure


object TestStructure {

  def from(structure: Structure): TestStructure = {
    val forwardPath: Option[TestStructurePath] = {
      structure.forwardPath.map { path =>
        TestStructurePath(
          path.startNodeId,
          path.endNodeId,
          path.nodeIds
        )
      }
    }

    val backwardPath: Option[TestStructurePath] = {
      structure.backwardPath.map { path =>
        TestStructurePath(
          path.startNodeId,
          path.endNodeId,
          path.nodeIds
        )
      }
    }

    val otherPaths: Seq[TestStructurePath] = {
      structure.otherPaths.map { path =>
        val nodeIds: Seq[Long] = path.elements.flatMap(_.nodeIds)
        TestStructurePath(
          path.startNodeId,
          path.endNodeId,
          nodeIds
        )
      }
    }

    TestStructure(
      forwardPath,
      backwardPath,
      otherPaths
    )
  }
}

case class TestStructure(
  forwardPath: Option[TestStructurePath] = None,
  backwardPath: Option[TestStructurePath] = None,
  otherPaths: Seq[TestStructurePath] = Seq.empty
)
