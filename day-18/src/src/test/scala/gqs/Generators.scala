package gqs

import org.scalacheck.Gen

import scala.util.Random

object Generators {
  private val validSymbols = Set('☃', '❄', '0', '*', '✦')
  private val otherChars: Gen[Char] = Gen.oneOf(
    ('a' to 'z') ++ ('A' to 'Z') ++ ('1' to '9') ++ Seq(' ', '!', '@', '#', '$', '%', '^', '&', '(', ')', '-', '_', '=', '+')
      .filterNot(validSymbols.contains)
  )

  val invalidGQS: Gen[String] = for {
    numberOfValidSymbols <- Gen.choose(0, 100)
    numberOfInvalidSymbols <- Gen.choose(1, 5)
    validPart <- Gen.listOfN(numberOfValidSymbols, Gen.oneOf(validSymbols.toSeq))
    invalidPart <- Gen.listOfN(numberOfInvalidSymbols, otherChars)
    combined = Random.shuffle(validPart ++ invalidPart)
  } yield combined.mkString

  val validGQS: Gen[String] = for {
    length <- Gen.choose(1, 10)
    gqs <- Gen.listOfN(length, validSymbols)
  } yield gqs.flatten.mkString
}
