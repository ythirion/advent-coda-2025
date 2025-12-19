package gqs

import scala.io.Source

object FileUtils {
  def readFile(filename: String): List[String] =
    Source.fromInputStream(getClass.getResourceAsStream(s"/$filename"))
      .getLines()
      .toList
}