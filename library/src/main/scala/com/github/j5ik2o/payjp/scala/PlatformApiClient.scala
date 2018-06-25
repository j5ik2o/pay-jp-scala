package com.github.j5ik2o.payjp.scala

import java.time.ZonedDateTime

import com.github.j5ik2o.payjp.scala.model.PlatformMerchant.OtherFee
import com.github.j5ik2o.payjp.scala.model._
import com.github.j5ik2o.payjp.scala.model.merchant._
import monix.eval.Task

trait PlatformApiClient {
  def getAccount(): Task[Account]

  /**
    * マーチャント作成。
    *
    * @param name マーチャント名
    * @return プラットフォームマーチャント
    */
  def createPlatformMerchant(name: String): Task[PlatformMerchant]

  /**
    * マーチャント情報を取得。
    *
    * @param accountId アカウントID
    * @return プラットフォームマーチャント
    */
  def getPlatformMerchant(accountId: AccountId): Task[PlatformMerchant]

  /**
    * マーチャントリストの取得。
    *
    * @param limit 取得するデータ数の最大値(1~100まで)
    * @param offset 基準点からのデータ取得を行う開始位置
    * @param since ここに指定したタイムスタンプ以降に作成されたデータを取得
    * @param until ここに指定したタイムスタンプ以前に作成されたデータを取得
    * @return プラットフォームマーチャントのリスト
    */
  def getPlatformMerchants(limit: Option[Int] = None,
                           offset: Option[Int] = None,
                           since: Option[ZonedDateTime] = None,
                           until: Option[ZonedDateTime] = None): Task[Collection[PlatformMerchant]]

  /**
    * マーチャントのAPIキーの更新。
    *
    * @param accountId アカウントID
    * @param keytype APIキータイプ(public, secret）
    * @param accessmode 動作モード(livemode, testmode)
    * @param timing 更新タイミング、即時もしくは24時間後(now, 24hours)
    * @return
    */
  def updatePlatformMerchantKeys(accountId: AccountId,
                                 keytype: ApiKeyType,
                                 accessmode: String,
                                 timing: String): Task[PlatformMerchant]

  /**
    * マーチャントの基本情報登録。
    *
    * @param productName
    * @param url
    * @return
    */
  def updatePlatformMerchantBasic(accountId: AccountId,
                                  dryRun: Boolean,
                                  productName: ProductName,
                                  url: String,
                                  serviceStartAt: String,
                                  usingService: Option[String],
                                  productDetail: String,
                                  productDetailDocument: Option[String],
                                  deleteProductDetailDocument: Option[Int],
                                  productPrice: ProductPrice,
                                  businessType: BusinessType,
                                  businessName: Name,
                                  dateOfEstablishment: String,
                                  businessCapital: Int,
                                  presidentLastName: Name,
                                  presidentFirstName: Name,
                                  presidentBirthAt: String,
                                  presidentGender: GenderType,
                                  address: Address,
                                  contact: Contact,
                                  scl: Option[Scl],
                                  bank: Bank,
                                  corporateNumber: Option[String],
                                  licenseCert: Option[String],
                                  deleteLicenseCert: Option[Int]): Task[PlatformMerchant]

  /**
    * マーチャントの追加情報登録。
    *
    * @return
    */
  def updatePlatformMerchantAdditional(accountId: AccountId,
                                       dryRun: Boolean,
                                       productType: ProductType,
                                       chargeType: ChargeType,
                                       soleProp: Boolean,
                                       dateOfEstablishment: Option[String],
                                       openingBusinessCert: Option[String],
                                       sitePublished: Boolean,
                                       businessSalesLastYear: Int,
                                       businessDetail: String,
                                       shopAddress: Option[Address],
                                       shopPhone: Option[String],
                                       privacyPolicyUrl: String,
                                       sslEnabled: Boolean,
                                       otherFee: Option[OtherFee],
                                       contactPersonLastName: String,
                                       contactPersonFirstName: String,
                                       contactPhone: String): Task[PlatformMerchant]

  /**
    * マーチャントの削除。
    *
    * @param accountId アカウントID
    * @return 削除結果
    */
  def deletePlatformMerchant(accountId: AccountId): Task[Deleted[AccountId]]

  /**
    * プラットフォーム入金情報を取得
    *
    * @see https://pay.jp/docs/api/#%E3%83%97%E3%83%A9%E3%83%83%E3%83%88%E3%83%95%E3%82%A9%E3%83%BC%E3%83%9E%E3%83%BC%E3%81%AE%E5%85%A5%E9%87%91%E6%83%85%E5%A0%B1%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param platformTransferId プラットフォーム入金ID
    * @return プラットフォーム入金
    */
  def getPlatformTransfer(platformTransferId: PlatformTransferId): Task[PlatformTransfer]

  /**
    * プラットフォーム入金のリストを取得
    *
    * @see https://pay.jp/docs/api/#%E3%83%97%E3%83%A9%E3%83%83%E3%83%88%E3%83%95%E3%82%A9%E3%83%BC%E3%83%9E%E3%83%BC%E3%81%AE%E5%85%A5%E9%87%91%E3%83%AA%E3%82%B9%E3%83%88%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param status ステータス
    * @param limit 取得するデータ数の最大値(1~100まで)
    * @param offset 基準点からのデータ取得を行う開始位置
    * @param since ここに指定したタイムスタンプ以降に作成されたデータを取得
    * @param until ここに指定したタイムスタンプ以前に作成されたデータを取得
    * @return プラットフォーム入金のリスト
    */
  def getPlatformTransfers(status: Option[TransferStatusType] = None,
                           limit: Option[Int] = None,
                           offset: Option[Int] = None,
                           since: Option[ZonedDateTime] = None,
                           until: Option[ZonedDateTime] = None): Task[Collection[PlatformTransfer]]

  /**
    * マーチャント入金情報を取得
    *
    * @see https://pay.jp/docs/api/#%E3%83%9E%E3%83%BC%E3%83%81%E3%83%A3%E3%83%B3%E3%83%88%E3%81%AE%E5%85%A5%E9%87%91%E6%83%85%E5%A0%B1%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param platformTransferId プラットフォーム入金ID
    * @param transferId 入金ID
    * @return
    */
  def getMerchantTransfer(platformTransferId: PlatformTransferId, transferId: TransferId): Task[Transfer]

  /**
    * マーチャント入金のリストを取得
    *
    * @see https://pay.jp/docs/api/#%E3%83%9E%E3%83%BC%E3%83%81%E3%83%A3%E3%83%B3%E3%83%88%E3%81%AE%E5%85%A5%E9%87%91%E3%83%AA%E3%82%B9%E3%83%88%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param platformTransferId プラットフォーム入金ID
    * @return マーチャント入金のリスト
    */
  def getMerchantTransfers(platformTransferId: PlatformTransferId): Task[Collection[Transfer]]
}
