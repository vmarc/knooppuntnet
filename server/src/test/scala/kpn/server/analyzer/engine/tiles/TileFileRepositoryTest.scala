package kpn.server.analyzer.engine.tiles

import java.io.File

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.domain.OldTile
import org.apache.commons.io.FileUtils

class TileFileRepositoryTest extends UnitTest {

  test("existing mvt tile names") {

    try {
      createFile("/tmp/tiles/inline-skating/11/1048/676.mvt")
      createFile("/tmp/tiles/motorboat/11/1048/676.mvt")
      createFile("/tmp/tiles/motorboat/11/1048/677.mvt")

      val repo = new TileFileRepositoryImpl("/tmp/tiles", "mvt")

      repo.existingTileNames("inline-skating", 11) should equal(Seq("inline-skating-11-1048-676"))
      repo.existingTileNames("motorboat", 11) should equal(Seq("motorboat-11-1048-676", "motorboat-11-1048-677"))
    }
    finally {
      FileUtils.deleteDirectory(new File("/tmp/tiles"))
    }
  }

  test("existing png tile names") {

    try {
      createFile("/tmp/tiles/horse-riding/surface/7/64/41.png")
      createFile("/tmp/tiles/horse-riding/surface/7/65/42.png")
      createFile("/tmp/tiles/horse-riding/surface/7/65/43.png")

      val repo = new TileFileRepositoryImpl("/tmp/tiles", "png")

      repo.existingTileNames("horse-riding/surface", 7) should equal(
        Seq(
          "horse-riding-surface-7-64-41",
          "horse-riding-surface-7-65-42",
          "horse-riding-surface-7-65-43",
        )
      )
    }
    finally {
      FileUtils.deleteDirectory(new File("/tmp/tiles"))
    }
  }

  test("delete mvt tiles") {

    try {

      val repo = new TileFileRepositoryImpl("/tmp/tiles", "mvt")

      val tileName1 = "inline-skating-11-1048-676"
      val tileName2 = "motorboat-11-1048-676"

      val tileFile1 = createFile("/tmp/tiles/inline-skating/11/1048/676.mvt")
      val tileFile2 = createFile("/tmp/tiles/motorboat/11/1048/676.mvt")
      val tileFile3 = createFile("/tmp/tiles/motorboat/11/1048/677.mvt")

      assert(tileFile1.exists())
      assert(tileFile2.exists())
      assert(tileFile3.exists())

      repo.delete(Seq(tileName1, tileName2))

      assert(!tileFile1.exists())
      assert(!tileFile2.exists())
      assert(tileFile3.exists())

      assert(!new File("/tmp/tiles/inline-skating/11/1048").exists())
      assert(new File("/tmp/tiles/motorboat/11/1048").exists())
    }
    finally {
      FileUtils.deleteDirectory(new File("/tmp/tiles"))
    }
  }

  test("delete png tiles") {

    try {
      val repo = new TileFileRepositoryImpl("/tmp/tiles", "png")

      val tileName1 = "inline-skating-11-1049-676"
      val tileName2 = "inline-skating-surface-11-1049-676"
      val tileName3 = "inline-skating-survey-11-1049-676"
      val tileName4 = "inline-skating-analysis-11-1049-676"

      val tileFile1 = createFile("/tmp/tiles/inline-skating/11/1049/676.png")
      val tileFile2 = createFile("/tmp/tiles/inline-skating/surface/11/1049/676.png")
      val tileFile3 = createFile("/tmp/tiles/inline-skating/survey/11/1049/676.png")
      val tileFile4 = createFile("/tmp/tiles/inline-skating/analysis/11/1049/676.png")
      val tileFile5 = createFile("/tmp/tiles/inline-skating/analysis/11/1049/677.png")

      assert(tileFile1.exists())
      assert(tileFile2.exists())
      assert(tileFile3.exists())
      assert(tileFile4.exists())
      assert(tileFile5.exists())

      repo.delete(Seq(tileName1, tileName2, tileName3, tileName4))

      assert(!tileFile1.exists())
      assert(!tileFile2.exists())
      assert(!tileFile3.exists())
      assert(!tileFile4.exists())
      assert(tileFile5.exists())

      assert(!new File("/tmp/tiles/inline-skating/11/1049").exists())
      assert(!new File("/tmp/tiles/inline-skating/surface/11/1049").exists())
      assert(!new File("/tmp/tiles/inline-skating/survey/11/1049").exists())
      assert(new File("/tmp/tiles/inline-skating/analysis/11/1049").exists())
    }
    finally {
      FileUtils.deleteDirectory(new File("/tmp/tiles"))
    }
  }

  test("deleteTile") {

    try {
      val file = new File("/tmp/tiles/cycling/survey/11/12/13.png")

      val repo = new TileFileRepositoryImpl("/tmp/tiles", "png")

      repo.saveOrUpdate("cycling/survey", OldTile(11, 12, 13), Array())
      assert(file.exists())

      repo.deleteTile("cycling/survey", OldTile(11, 12, 13))
      assert(!file.exists())
    }
    finally {
      FileUtils.deleteDirectory(new File("/tmp/tiles"))
    }
  }

  private def createFile(fileName: String): File = {
    val file = new File(fileName)
    FileUtils.touch(file)
    file
  }

}
