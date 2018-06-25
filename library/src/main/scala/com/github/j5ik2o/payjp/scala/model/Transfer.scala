package com.github.j5ik2o.payjp.scala.model

import java.time.{ LocalDate, ZonedDateTime }

import cats.Eq
import io.circe.Decoder

case class TransferId(value: String) {
  require(value.startsWith("tr_"))
}

object TransferId {
  implicit object TransferIdEq extends Eq[TransferId] {
    override def eqv(x: TransferId, y: TransferId): Boolean = x == y
  }
  implicit val TransferDecoder: Decoder[TransferId] = Decoder[String].map(TransferId(_))
}

case class Transfer(id: TransferId,
                    liveMode: Boolean,
                    created: ZonedDateTime,
                    amount: Amount,
                    currency: Currency,
                    status: TransferStatusType,
                    charges: Collection[Charge],
                    scheduledDate: LocalDate,
                    summary: Summary,
                    descriptionOpt: Option[String] = None,
                    termStart: ZonedDateTime,
                    termEnd: ZonedDateTime,
                    transferAmountOpt: Option[Int],
                    transferDateOpt: Option[ZonedDateTime],
                    carriedBalanceOpt: Option[Int])

object Transfer extends JsonImplicits {

  implicit object TrasferEq extends Eq[Transfer] {
    override def eqv(x: Transfer, y: Transfer): Boolean = x == y
  }

  implicit val TransferDecoder: Decoder[Transfer] =
    Decoder.forProduct15(
      "id",
      "livemode",
      "created",
      "amount",
      "currency",
      "status",
      "charges",
      "scheduled_date",
      "summary",
      "description",
      "term_start",
      "term_end",
      "transfer_amount",
      "transfer_date",
      "carried_balance"
    )(
      Transfer.apply
    )

}
