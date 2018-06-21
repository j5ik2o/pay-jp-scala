package com.github.j5ik2o.payjp.scala.model

import java.time.{ LocalDate, ZonedDateTime }

import io.circe.Decoder

case class TransferId(value: String) {
  require(value.startsWith("tr_"))
}

object TransferId {
  implicit val TransferDecoder: Decoder[TransferId] = Decoder.decodeString.map(TransferId(_))
}

case class Transfer(id: TransferId,
                    liveMode: Boolean,
                    created: ZonedDateTime,
                    amount: Amount,
                    currency: Currency,
                    status: TransferStatus,
                    charges: Collection[Charge],
                    scheduledDate: LocalDate,
                    summary: Summary,
                    description: Option[String] = None,
                    termStart: ZonedDateTime,
                    termEnd: ZonedDateTime,
                    transferAmount: Option[Int],
                    transferDate: Option[ZonedDateTime],
                    carriedBalance: Option[Int])

object Transfer extends JsonImplicits {

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
