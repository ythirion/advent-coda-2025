package gqs

import gqs.Generators.{invalidGQS, validGQS}
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties
import org.scalatest.EitherValues

object GQSProperties extends Properties("GQS") with EitherValues {
  property("fail when containing an invalid characters") = forAll(invalidGQS) { input =>
    GQS.parseToDecimal(input) == Left(Error("Not a valid GQS 🥶"))
  }

  property("succeed for valid GQS") = forAll(validGQS) { gqs =>
    GQS.parseToDecimal(gqs).isRight
  }
}