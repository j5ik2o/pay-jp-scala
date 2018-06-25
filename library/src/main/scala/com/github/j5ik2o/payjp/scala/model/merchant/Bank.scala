package com.github.j5ik2o.payjp.scala.model.merchant

import cats.Eq

case class Bank(code: String, branchCode: String, `type`: BankAccountType, accountNumber: String, personName: String)

object Bank {

  implicit object BankEq extends Eq[Bank] {
    override def eqv(x: Bank, y: Bank): Boolean = x == y
  }

}
