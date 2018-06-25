package com.github.j5ik2o.payjp.scala.model

import cats.Eq
import cats.kernel.Monoid
import io.circe.Decoder

case class Collection[A](count: Int, data: Seq[A], hasMore: Boolean, url: String)

object Collection {

  implicit def collectionEq[A]: Eq[Collection[A]] =
    (x: Collection[A], y: Collection[A]) => x.data == y.data

  implicit def collectionMonoid[A]: Monoid[Collection[A]] = new Monoid[Collection[A]] {
    override def empty: Collection[A] = Collection[A](0, Seq.empty, hasMore = false, "")

    override def combine(x: Collection[A], y: Collection[A]): Collection[A] =
      Collection(x.count + y.count, x.data ++ y.data, x.hasMore || y.hasMore, x.url)
  }

  implicit def CardsRefDecoder[A: Decoder]: Decoder[Collection[A]] =
    Decoder.forProduct4("count", "data", "has_more", "url")(Collection.apply)

}
