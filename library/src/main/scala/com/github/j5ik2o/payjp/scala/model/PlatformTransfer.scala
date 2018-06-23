package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import cats.Eq
import io.circe.Decoder

case class PlatformTransferId(value: String) {
  require(value.startsWith("pf_tr_"))
}

object PlatformTransferId {

  implicit object PlatformTransferIdEq extends Eq[PlatformTransferId] {
    override def eqv(x: PlatformTransferId, y: PlatformTransferId): Boolean = x == y
  }

  implicit val PlatformTransferIdDecoder: Decoder[PlatformTransferId] = Decoder.decodeString.map(PlatformTransferId(_))

}

case class PlatformTransfer(id: PlatformTransferId,
                            freeAmount: Amount,
                            currency: Currency,
                            status: String,
                            transfers: Seq[Transfer],
                            scheduledDate: String,
                            summary: Summary,
                            termStart: ZonedDateTime,
                            termEnd: ZonedDateTime,
                            created: ZonedDateTime)

object PlatformTransfer extends JsonImplicits {

  implicit object PlatformTransferEq extends Eq[PlatformTransfer] {
    override def eqv(x: PlatformTransfer, y: PlatformTransfer): Boolean = x == y
  }

  implicit val PlatformTransferDecoder: Decoder[PlatformTransfer] =
    Decoder.forProduct10("id",
                         "free_amount",
                         "currency",
                         "status",
                         "transfers",
                         "scheduled_date",
                         "summary",
                         "term_start",
                         "term_end",
                         "created")(
      PlatformTransfer.apply
    )
}
