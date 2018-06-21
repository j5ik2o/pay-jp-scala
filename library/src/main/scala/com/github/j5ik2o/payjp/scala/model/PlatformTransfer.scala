package com.github.j5ik2o.payjp.scala.model

import java.time.ZonedDateTime

import io.circe.Decoder

case class PlatformTransferId(value: String) {
  require(value.startsWith("pf_tr_"))
}

object PlatformTransferId {
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
