package com.github.j5ik2o.payjp.scala

import java.time.ZonedDateTime

import com.github.j5ik2o.payjp.scala.model.PlatformMerchant.OtherFee
import com.github.j5ik2o.payjp.scala.model._
import com.github.j5ik2o.payjp.scala.model.merchant._
import monix.eval.Task

trait PlatformApiClient {

  /**
    * マーチャント作成。
    *
    * @see https://pay.jp/docs/api/#%E3%83%9E%E3%83%BC%E3%83%81%E3%83%A3%E3%83%B3%E3%83%88%E4%BD%9C%E6%88%90
    *
    * @param name マーチャント名
    * @return プラットフォームマーチャント
    */
  def createPlatformMerchant(name: String): Task[PlatformMerchant]

  /**
    * マーチャント情報を取得。
    *
    * @see https://pay.jp/docs/api/#%E3%83%9E%E3%83%BC%E3%83%81%E3%83%A3%E3%83%B3%E3%83%88%E6%83%85%E5%A0%B1%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param accountId アカウントID
    * @return プラットフォームマーチャント
    */
  def getPlatformMerchant(accountId: AccountId): Task[PlatformMerchant]

  /**
    * マーチャントリストの取得。
    *
    * @see https://pay.jp/docs/api/#%E3%83%9E%E3%83%BC%E3%83%81%E3%83%A3%E3%83%B3%E3%83%88%E3%83%AA%E3%82%B9%E3%83%88%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param limitOpt 取得するデータ数の最大値(1~100まで)
    * @param offsetOpt 基準点からのデータ取得を行う開始位置
    * @param sinceOpt ここに指定したタイムスタンプ以降に作成されたデータを取得
    * @param untilOpt ここに指定したタイムスタンプ以前に作成されたデータを取得
    * @return プラットフォームマーチャントのリスト
    */
  def getPlatformMerchants(limitOpt: Option[Int] = None,
                           offsetOpt: Option[Int] = None,
                           sinceOpt: Option[ZonedDateTime] = None,
                           untilOpt: Option[ZonedDateTime] = None): Task[Collection[PlatformMerchant]]

  /**
    * マーチャントのAPIキーの更新。
    *
    * @see https://pay.jp/docs/api/#%E3%83%9E%E3%83%BC%E3%83%81%E3%83%A3%E3%83%B3%E3%83%88%E3%81%AEapi%E3%82%AD%E3%83%BC%E3%81%AE%E6%9B%B4%E6%96%B0
    *
    * @param accountId アカウントID
    * @param keyType APIキータイプ(public, secret）
    * @param accessModeType 動作モード(livemode, testmode)
    * @param timingType 更新タイミング、即時もしくは24時間後(now, 24hours)
    * @return プラットフォームマーチャント
    */
  def updatePlatformMerchantKeys(accountId: AccountId,
                                 keyType: ApiKeyType,
                                 accessModeType: AccessModeType,
                                 timingType: TimingType): Task[PlatformMerchant]

  /**
    * マーチャントの基本情報登録。
    *
    * @see https://pay.jp/docs/api/#%E3%83%9E%E3%83%BC%E3%83%81%E3%83%A3%E3%83%B3%E3%83%88%E3%81%AE%E5%9F%BA%E6%9C%AC%E6%83%85%E5%A0%B1%E7%99%BB%E9%8C%B2
    *
    * @param productName プロダクト名
    * @param url URL
    * @return プラットフォームマーチャント
    */
  def updatePlatformMerchantBasic(accountId: AccountId,
                                  dryRun: Boolean,
                                  productName: ProductName,
                                  url: String,
                                  serviceStartAt: String,
                                  usingServiceOpt: Option[String],
                                  productDetail: String,
                                  productDetailDocumentOpt: Option[String],
                                  deleteProductDetailDocumentOpt: Option[Int],
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
                                  sclOpt: Option[Scl],
                                  bank: Bank,
                                  corporateNumberOpt: Option[String],
                                  licenseCertOpt: Option[String],
                                  deleteLicenseCertOpt: Option[Int]): Task[PlatformMerchant]

  /**
    * マーチャントの追加情報登録。
    *
    * @see https://pay.jp/docs/api/#%E3%83%9E%E3%83%BC%E3%83%81%E3%83%A3%E3%83%B3%E3%83%88%E3%81%AE%E8%BF%BD%E5%8A%A0%E6%83%85%E5%A0%B1%E7%99%BB%E9%8C%B2
    *
    * @return プラットフォームマーチャント
    */
  def updatePlatformMerchantAdditional(accountId: AccountId,
                                       dryRun: Boolean,
                                       productType: ProductType,
                                       chargeType: ChargeType,
                                       soleProp: Boolean,
                                       dateOfEstablishmentOpt: Option[String],
                                       openingBusinessCertOpt: Option[String],
                                       sitePublished: Boolean,
                                       businessSalesLastYear: Int,
                                       businessDetail: String,
                                       shopAddressOpt: Option[Address],
                                       shopPhoneOpt: Option[String],
                                       privacyPolicyUrl: String,
                                       sslEnabled: Boolean,
                                       otherFeeOpt: Option[OtherFee],
                                       contactPersonLastName: String,
                                       contactPersonFirstName: String,
                                       contactPhone: String): Task[PlatformMerchant]

  /**
    * マーチャントの削除。
    *
    * @see https://pay.jp/docs/api/#%E3%83%9E%E3%83%BC%E3%83%81%E3%83%A3%E3%83%B3%E3%83%88%E3%81%AE%E5%89%8A%E9%99%A4
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
    * @param statusOpt ステータス
    * @param limitOpt 取得するデータ数の最大値(1~100まで)
    * @param offsetOpt 基準点からのデータ取得を行う開始位置
    * @param sinceOpt ここに指定したタイムスタンプ以降に作成されたデータを取得
    * @param untilOpt ここに指定したタイムスタンプ以前に作成されたデータを取得
    * @return プラットフォーム入金のリスト
    */
  def getPlatformTransfers(statusOpt: Option[TransferStatusType] = None,
                           limitOpt: Option[Int] = None,
                           offsetOpt: Option[Int] = None,
                           sinceOpt: Option[ZonedDateTime] = None,
                           untilOpt: Option[ZonedDateTime] = None): Task[Collection[PlatformTransfer]]

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
