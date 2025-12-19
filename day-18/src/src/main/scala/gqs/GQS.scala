package gqs

case class Error(message: String);

object GQS {
  private val symbols: Map[Char, Int] = Map(
    '☃' -> -2,
    '❄' -> -1,
    '0' -> 0,
    '*' -> +1,
    '✦' -> +2
  )

  def parseToDecimal(input: String): Either[Error, Int] =
    if (isValidGQS(input)) Right(toDecimal(input))
    else Left(Error("Not a valid GQS 🥶"))

  private def isValidGQS(input: String): Boolean =
    input.forall(c => symbols.contains(c))

  private def toDecimal(gqs: String): Int =
    gqs.reverse
      .zipWithIndex
      .map { (char, index) =>
        symbols(char) * Math.pow(5, index).toInt
      }.sum
}