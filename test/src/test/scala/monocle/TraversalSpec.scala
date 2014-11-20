package monocle

import monocle.law.TraversalLaws

import scalaz.Equal
import monocle.TestUtil._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.specs2.scalaz.Spec

class TraversalSpec extends Spec {

  case class Location(latitude: Int, longitude: Int, name: String)

  val locationTraversal = PTraversal.apply2[Location, Location, Int, Int](_.latitude, _.longitude) {
    case (newLat, newLong, oldLoc) =>
      oldLoc.copy(latitude = newLat, longitude = newLong)
  }

  implicit val locationGen: Arbitrary[Location] = Arbitrary(for {
    x <- arbitrary[Int]
    y <- arbitrary[Int]
    n <- arbitrary[String]
  } yield Location(x, y, n))

  implicit val exampleEq = Equal.equalA[Location]

  checkAll(TraversalLaws(locationTraversal))

}
