package gqs

import gqs.ValidGQSTest.examples
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.prop.TableDrivenPropertyChecks
import org.scalatest.prop.Tables.Table

class ValidGQSTest
  extends AnyFlatSpec
    with Matchers
    with TableDrivenPropertyChecks {

  "parseToDecimal" should "return its decimal value" in {
    forAll(examples) { (gqs, expectedValue) =>
      GQS.parseToDecimal(gqs) shouldBe Right(expectedValue)
    }
  }

  "calculate avg for gqs file" should "return -288.77" in {
    calculateAverageFor(FileUtils.readFile("gqs.txt")) shouldBe -288.7762
  }

  private def calculateAverageFor(lines: List[String]): Double =
    lines.map(GQS.parseToDecimal)
      .collect { case Right(value) => value }
      .sum.toDouble / lines.length
}

object ValidGQSTest {
  private val examples =
    Table(
      ("gqs", "expectedValue"),
      ("☃", -2),
      ("❄", -1),
      ("0", 0),
      ("*", +1),
      ("✦", +2),
      ("✦*0❄", 274),
      ("✦**", 56),
      ("✦00*0❄✦", 31372)
    )
}