package monocle

import scalaz.Monoid


/**
 * A [[Getter]] can be seen as a glorified get method between
 * a type S and a type A.
 *
 * A [[Getter]] is also a valid [[Fold]]
 *
 * @tparam S the source of the [[Getter]]
 * @tparam A the target of the [[Getter]]
 */
final case class Getter[S, A](get: S => A) {

  /*************************************************************/
  /** Compose methods between a [[Getter]] and another Optics  */
  /*************************************************************/

  /** compose a [[Getter]] with a [[Fold]] */
  @inline def composeFold[B](other: Fold[A, B]): Fold[S, B] =
    asFold composeFold other

  /** compose a [[Getter]] with a [[Getter]] */
  @inline def composeGetter[B](other: Getter[A, B]): Getter[S, B] =
    Getter(other.get compose get)

  /** compose a [[Getter]] with a [[PTraversal]] */
  @inline def composeTraversal[B, C, D](other: PTraversal[A, B, C, D]): Fold[S, C] =
    asFold composeTraversal other

  /** compose a [[Getter]] with a [[POptional]] */
  @inline def composeOptional[B, C, D](other: POptional[A, B, C, D]): Fold[S, C] =
    asFold composeOptional other

  /** compose a [[Getter]] with a [[PPrism]] */
  @inline def composePrism[B, C, D](other: PPrism[A, B, C, D]): Fold[S, C] =
    asFold composePrism other

  /** compose a [[Getter]] with a [[PLens]] */
  @inline def composeLens[B, C, D](other: PLens[A, B, C, D]): Getter[S, C] =
    composeGetter(other.asGetter)

  /** compose a [[Getter]] with a [[PIso]] */
  @inline def composeIso[B, C, D](other: PIso[A, B, C, D]): Getter[S, C] =
    composeGetter(other.asGetter)


  /******************************************************************/
  /** Transformation methods to view a [[Getter]] as another Optics */
  /******************************************************************/

  /** view a [[Getter]] with a [[Fold]] */
  def asFold: Fold[S, A] = new Fold[S, A]{
    @inline def foldMap[M: Monoid](f: A => M)(s: S): M =
      f(get(s))
  }

}