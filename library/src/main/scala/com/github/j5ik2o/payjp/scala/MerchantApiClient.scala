package com.github.j5ik2o.payjp.scala

import java.time.ZonedDateTime

import com.github.j5ik2o.payjp.scala.model._
import monix.eval.Task

trait MerchantApiClient {

  /**
    * アカウント情報を取得。
    *
    * @return アカウント
    */
  def getAccount(): Task[Account]

  /**
    * 顧客を作成。
    *
    * @see https://pay.jp/docs/api/#%E9%A1%A7%E5%AE%A2%E3%82%92%E4%BD%9C%E6%88%90
    *
    * @param customerIdOpt 顧客ID
    * @param emailOpt メールアドレス
    * @param descriptionOpt 説明
    * @param tokenIdOpt トークン
    * @param metadata メタデータ
    * @return 顧客
    */
  def createCustomer(customerIdOpt: Option[CustomerId] = None,
                     emailOpt: Option[String] = None,
                     descriptionOpt: Option[String] = None,
                     tokenIdOpt: Option[TokenId] = None,
                     metadata: Map[String, Any] = Map.empty): Task[Customer]

  /**
    * 顧客情報を取得。
    *
    * @see https://pay.jp/docs/api/#%E9%A1%A7%E5%AE%A2%E6%83%85%E5%A0%B1%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param customerId 顧客ID
    * @return 顧客
    */
  def getCustomer(customerId: CustomerId): Task[Customer]

  /**
    * 顧客情報を更新。
    *
    * @see https://pay.jp/docs/api/#%E9%A1%A7%E5%AE%A2%E6%83%85%E5%A0%B1%E3%82%92%E6%9B%B4%E6%96%B0
    *
    * @param customerId 顧客ID
    * @param emailOpt メールアドレス
    * @param descriptionOpt 説明
    * @param defaultCardOpt デフォルトカード
    * @param tokenIdOpt トークン
    * @param metadata メータデータ
    * @return 顧客
    */
  def updateCustomer(customerId: CustomerId,
                     emailOpt: Option[String] = None,
                     descriptionOpt: Option[String] = None,
                     defaultCardOpt: Option[String] = None,
                     tokenIdOpt: Option[TokenId] = None,
                     metadata: Map[String, String] = Map.empty): Task[Customer]

  /**
    * 顧客を削除。
    *
    * @see https://pay.jp/docs/api/#%E9%A1%A7%E5%AE%A2%E3%82%92%E5%89%8A%E9%99%A4
    *
    * @param customerId 顧客ID
    * @return 削除結果
    */
  def deleteCustomer(customerId: CustomerId): Task[Deleted[CustomerId]]

  /**
    * 顧客リストの取得。
    *
    * @see https://pay.jp/docs/api/#%E9%A1%A7%E5%AE%A2%E3%83%AA%E3%82%B9%E3%83%88%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param limitOpt 取得するデータ数の最大値(1~100まで)
    * @param offsetOpt 基準点からのデータ取得を行う開始位置
    * @param sinceOpt ここに指定したタイムスタンプ以降に作成されたデータを取得
    * @param untilOpt ここに指定したタイムスタンプ以前に作成されたデータを取得
    * @return 顧客の集合
    */
  def getCustomers(limitOpt: Option[Int] = None,
                   offsetOpt: Option[Int] = None,
                   sinceOpt: Option[ZonedDateTime] = None,
                   untilOpt: Option[ZonedDateTime] = None): Task[Collection[Customer]]

  // --- Plan API
  /**
    * プランを作成。
    *
    * @see https://pay.jp/docs/api/#plan-%E3%83%97%E3%83%A9%E3%83%B3
    *
    * @param amount 金額
    * @param currency 通貨
    * @param interval 課金期間
    * @param idOpt プランID
    * @param nameOpt プラン名
    * @param trialDaysOpt トライアル日数
    * @param billingDayOpt 月次プランに指定可能な課金日(1〜31)
    * @param metadata メタデータ
    * @return プラン
    */
  def createPlan(amount: Amount,
                 currency: Currency,
                 interval: PlanIntervalType,
                 idOpt: Option[String] = None,
                 nameOpt: Option[String] = None,
                 trialDaysOpt: Option[Int] = None,
                 billingDayOpt: Option[Int] = None,
                 metadata: Map[String, String] = Map.empty): Task[Plan]

  /**
    * プラン情報を取得。
    *
    * @see https://pay.jp/docs/api/#%E3%83%97%E3%83%A9%E3%83%B3%E6%83%85%E5%A0%B1%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param planId プランID
    * @return プラン
    */
  def getPlan(planId: PlanId): Task[Plan]

  /**
    * プランを更新
    *
    * @see https://pay.jp/docs/api/#%E3%83%97%E3%83%A9%E3%83%B3%E3%82%92%E6%9B%B4%E6%96%B0
    *
    * @param planId プランID
    * @param nameOpt 名前
    * @param metadata メタデータ
    * @return プラン
    */
  def updatePlan(planId: PlanId, nameOpt: Option[String] = None, metadata: Map[String, String] = Map.empty): Task[Plan]

  /**
    * プランの削除。
    *
    * @see https://pay.jp/docs/api/#%E3%83%97%E3%83%A9%E3%83%B3%E3%82%92%E5%89%8A%E9%99%A4
    *
    * @param planId プランID
    * @return 削除結果
    */
  def deletePlan(planId: PlanId): Task[Deleted[PlanId]]

  /**
    * プランリストを取得。
    *
    * @see https://pay.jp/docs/api/#%E3%83%97%E3%83%A9%E3%83%B3%E3%83%AA%E3%82%B9%E3%83%88%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param limitOpt 取得するデータ数の最大値(1~100まで)
    * @param offsetOpt 基準点からのデータ取得を行う開始位置
    * @param sinceOpt ここに指定したタイムスタンプ以降に作成されたデータを取得
    * @param untilOpt ここに指定したタイムスタンプ以前に作成されたデータを取得
    * @return プランリスト
    */
  def getPlans(limitOpt: Option[Int] = None,
               offsetOpt: Option[Int] = None,
               sinceOpt: Option[ZonedDateTime] = None,
               untilOpt: Option[ZonedDateTime] = None): Task[Collection[Plan]]

  // --- Subscription API
  /**
    * 定期課金を作成。
    *
    * @see https://pay.jp/docs/api/#%E5%AE%9A%E6%9C%9F%E8%AA%B2%E9%87%91%E3%82%92%E4%BD%9C%E6%88%90
    *
    * @param customerId 顧客ID
    * @param planId プランID
    * @param trialEndOpt トライアル終了日時
    * @param prorateOpt 日割り課金を設定するかどうか(デフォルトはfalse)
    * @param metadata メタデータ
    * @return 定期課金
    */
  def createSubscription(customerId: CustomerId,
                         planId: PlanId,
                         trialEndOpt: Option[ZonedDateTime] = None,
                         prorateOpt: Option[Boolean] = None,
                         metadata: Map[String, String] = Map.empty): Task[Subscription]

  /**
    * 定期課金を更新。
    *
    * @see https://pay.jp/docs/api/#%E5%AE%9A%E6%9C%9F%E8%AA%B2%E9%87%91%E3%82%92%E6%9B%B4%E6%96%B0
    *
    * @param subscriptionId 定期課金ID
    * @param planId プランID
    * @param trialEndOpt トライアル終了日時
    * @param prorateOpt 日割り課金を設定するかどうか(デフォルトはfalse)
    * @param metadata メタデータ
    * @return 定期課金
    */
  def updateSubscription(subscriptionId: SubscriptionId,
                         planId: Option[PlanId] = None,
                         trialEndOpt: Option[ZonedDateTime] = None,
                         prorateOpt: Option[Boolean] = None,
                         metadata: Map[String, String] = Map.empty): Task[Subscription]

  /**
    * 定期課金を停止
    *
    * @see https://pay.jp/docs/api/#%E5%AE%9A%E6%9C%9F%E8%AA%B2%E9%87%91%E3%82%92%E5%81%9C%E6%AD%A2
    *
    * @param subscriptionId 定期課金ID
    * @return 定期課金
    */
  def pauseSubscription(subscriptionId: SubscriptionId): Task[Subscription]

  /**
    * 定期課金を再開
    *
    * @see https://pay.jp/docs/api/#%E5%AE%9A%E6%9C%9F%E8%AA%B2%E9%87%91%E3%82%92%E5%86%8D%E9%96%8B
    *
    * @param subscriptionId 定期課金ID
    * @return 定期課金
    */
  def resumeSubscription(subscriptionId: SubscriptionId): Task[Subscription]

  /**
    * 定期課金をキャンセル
    *
    * @see https://pay.jp/docs/api/#%E5%AE%9A%E6%9C%9F%E8%AA%B2%E9%87%91%E3%82%92%E3%82%AD%E3%83%A3%E3%83%B3%E3%82%BB%E3%83%AB
    *
    * @param subscriptionId 定期課金ID
    * @return 定期課金
    */
  def cancelSubscription(subscriptionId: SubscriptionId): Task[Subscription]

  /**
    * 定期課金を削除。
    *
    * @see https://pay.jp/docs/api/#%E5%AE%9A%E6%9C%9F%E8%AA%B2%E9%87%91%E3%82%92%E5%89%8A%E9%99%A4
    *
    * @param subscriptionId 定期課金ID
    * @return 定期課金
    */
  def deleteSubscription(subscriptionId: SubscriptionId): Task[Deleted[SubscriptionId]]

  /**
    * 定期課金のリストを取得。
    *
    * @see https://pay.jp/docs/api/#%E5%AE%9A%E6%9C%9F%E8%AA%B2%E9%87%91%E3%81%AE%E3%83%AA%E3%82%B9%E3%83%88%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param planIdOpt プランID
    * @param statusOpt ステータス
    * @param limitOpt 取得するデータ数の最大値(1~100まで)
    * @param offsetOpt 基準点からのデータ取得を行う開始位置
    * @param sinceOpt ここに指定したタイムスタンプ以降に作成されたデータを取得
    * @param untilOpt ここに指定したタイムスタンプ以前に作成されたデータを取得
    * @return 定期課金のリスト
    */
  def getSubscriptions(planIdOpt: Option[PlanId] = None,
                       statusOpt: Option[SubscriptionStatusType] = None,
                       limitOpt: Option[Int] = None,
                       offsetOpt: Option[Int] = None,
                       sinceOpt: Option[ZonedDateTime] = None,
                       untilOpt: Option[ZonedDateTime] = None): Task[Collection[Subscription]]

  // --- Customer Subscription API

  /**
    * 顧客の定期課金を取得。
    *
    * @see https://pay.jp/docs/api/#%E9%A1%A7%E5%AE%A2%E3%81%AE%E5%AE%9A%E6%9C%9F%E8%AA%B2%E9%87%91%E6%83%85%E5%A0%B1%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param customerId 顧客ID
    * @param subscriptionId 定期課金ID
    * @return 定期課金
    */
  def getSubscriptionByCustomerId(customerId: CustomerId, subscriptionId: SubscriptionId): Task[Subscription]

  /**
    * 顧客の定期課金リストを取得。
    *
    * @see https://pay.jp/docs/api/#%E9%A1%A7%E5%AE%A2%E3%81%AE%E5%AE%9A%E6%9C%9F%E8%AA%B2%E9%87%91%E3%83%AA%E3%82%B9%E3%83%88%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param customerId 顧客ID
    * @param planIdOpt プランID
    * @param statusOpt ステータス
    * @param limitOpt 取得するデータ数の最大値(1~100まで)
    * @param offsetOpt 基準点からのデータ取得を行う開始位置
    * @param sinceOpt ここに指定したタイムスタンプ以降に作成されたデータを取得
    * @param untilOpt ここに指定したタイムスタンプ以前に作成されたデータを取得
    * @return 定期課金のリスト
    */
  def getSubscriptionsByCustomerId(customerId: CustomerId,
                                   planIdOpt: Option[PlanId] = None,
                                   statusOpt: Option[SubscriptionStatusType] = None,
                                   limitOpt: Option[Int] = None,
                                   offsetOpt: Option[Int] = None,
                                   sinceOpt: Option[ZonedDateTime] = None,
                                   untilOpt: Option[ZonedDateTime] = None): Task[Collection[Subscription]]

  // --- Charge API
  /**
    * 支払いを作成。
    *
    * @see https://pay.jp/docs/api/#%E6%94%AF%E6%89%95%E3%81%84%E3%82%92%E4%BD%9C%E6%88%90
    *
    * @param amountAndCurrencyOpt 金額と通貨単位
    * @param productIdOpt プロダクトID
    * @param customerIdOpt 顧客ID
    * @param tokenIdOpt トークンID
    * @param descriptionOpt 説明
    * @param captureOpt 支払い処理を確定するかどうか (falseの場合、カードの認証と支払い額の確保のみ行う)
    * @param expiryDaysOpt 認証状態が失効するまでの日数
    * @param metadata メタデータ
    * @param platformFeeOpt PAY.JP Platform のプラットフォーマーに振り分けられる入金金額(PAY.JP Platform で作成した Platform Merchant でのみ利用可能なパラメーター)
    * @return 支払い
    */
  def createCharge(amountAndCurrencyOpt: Option[(Amount, Currency)],
                   productIdOpt: Option[ProductId],
                   customerIdOpt: Option[CustomerId],
                   tokenIdOpt: Option[TokenId],
                   descriptionOpt: Option[String] = None,
                   captureOpt: Option[Boolean] = None,
                   expiryDaysOpt: Option[ZonedDateTime] = None,
                   metadata: Map[String, String] = Map.empty,
                   platformFeeOpt: Option[BigDecimal] = None): Task[Charge]

  /**
    * 支払い情報を取得。
    *
    * @see 支払い情報を取得
    *
    * @param chargeId 支払いID
    * @return 支払い
    */
  def getCharge(chargeId: ChargeId): Task[Charge]

  /**
    * 支払い情報を更新
    *
    * @see https://pay.jp/docs/api/#%E6%94%AF%E6%89%95%E3%81%84%E6%83%85%E5%A0%B1%E3%82%92%E6%9B%B4%E6%96%B0
    *
    * @param chargeId 支払いID
    * @param descriptionOpt 説明
    * @param metadata メタデータ
    * @return 支払い
    */
  def updateCharge(chargeId: ChargeId,
                   descriptionOpt: Option[String] = None,
                   metadata: Map[String, String] = Map.empty): Task[Charge]

  /**
    * 返金する。
    *
    * @see https://pay.jp/docs/api/#%E8%BF%94%E9%87%91%E3%81%99%E3%82%8B
    *
    * @param chargeId 支払いID
    * @param amountOpt 金額
    * @param refundReasonOpt 返金理由
    * @return
    */
  def refundCharge(chargeId: ChargeId,
                   amountOpt: Option[Amount] = None,
                   refundReasonOpt: Option[String] = None): Task[Charge]

  /**
    * 支払いを再認証する。
    *
    * @see https://pay.jp/docs/api/#%E6%94%AF%E6%89%95%E3%81%84%E3%82%92%E5%86%8D%E8%AA%8D%E8%A8%BC%E3%81%99%E3%82%8B
    *
    * @param chargeId 支払いID
    * @param expiryDaysOpt 認証状態が失効するまでの日数
    * @return 支払い
    */
  def reAuthCharge(chargeId: ChargeId, expiryDaysOpt: Option[Int] = None): Task[Charge]

  /**
    * 支払い処理を確定する。
    *
    * @see https://pay.jp/docs/api/#%E6%94%AF%E6%89%95%E3%81%84%E5%87%A6%E7%90%86%E3%82%92%E7%A2%BA%E5%AE%9A%E3%81%99%E3%82%8B
    *
    * @param chargeId 支払いID
    * @param amountOpt 金額
    * @return 支払い
    */
  def captureCharge(chargeId: ChargeId, amountOpt: Option[Amount] = None): Task[Charge]

  /**
    * 支払いリストを取得。
    *
    * @see https://pay.jp/docs/api/#%E6%94%AF%E6%89%95%E3%81%84%E3%83%AA%E3%82%B9%E3%83%88%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param customerIdOpt 顧客ID
    * @param subscriptionIdOpt 定期課金ID
    * @param limitOpt 取得するデータ数の最大値(1~100まで)
    * @param offsetOpt 基準点からのデータ取得を行う開始位置
    * @param sinceOpt ここに指定したタイムスタンプ以降に作成されたデータを取得
    * @param untilOpt ここに指定したタイムスタンプ以前に作成されたデータを取得
    * @return 支払いのリスト
    */
  def getCharges(customerIdOpt: Option[CustomerId] = None,
                 subscriptionIdOpt: Option[String] = None,
                 limitOpt: Option[Int] = None,
                 offsetOpt: Option[Int] = None,
                 sinceOpt: Option[ZonedDateTime] = None,
                 untilOpt: Option[ZonedDateTime] = None): Task[Collection[Charge]]

  /**
    * 入金に対する支払いリストを取得。
    *
    * @param transferId 入金ID
    * @param customerIdOpt 顧客ID
    * @param limitOpt 取得するデータ数の最大値(1~100まで)
    * @param offsetOpt 基準点からのデータ取得を行う開始位置
    * @param sinceOpt ここに指定したタイムスタンプ以降に作成されたデータを取得
    * @param untilOpt ここに指定したタイムスタンプ以前に作成されたデータを取得
    * @return 支払いのリスト
    */
  def getChargesByTransferId(transferId: TransferId,
                             customerIdOpt: Option[CustomerId] = None,
                             limitOpt: Option[Int] = None,
                             offsetOpt: Option[Int] = None,
                             sinceOpt: Option[ZonedDateTime] = None,
                             untilOpt: Option[ZonedDateTime] = None): Task[Collection[Charge]]

  // --- Product API
  /**
    * プロダクトを作成。
    *
    * @see https://pay.jp/docs/api/#%E3%83%97%E3%83%AD%E3%83%80%E3%82%AF%E3%83%88%E3%82%92%E4%BD%9C%E6%88%90
    *
    * @param amount 金額
    * @param currency ISOコード
    * @param invalidAfterOpt　このプロダクトの有効日時
    * @param captureOpt　このプロダクトと紐づいた支払いを作成した時に、支払い処理を確定するかどうか (falseの場合、カードの認証と支払い額の確保のみ行う)
    * @param metadata メタデータ
    * @return プロダクト
    */
  def createProduct(amount: Amount,
                    currency: Currency,
                    invalidAfterOpt: Option[ZonedDateTime] = None,
                    captureOpt: Option[Boolean] = None,
                    metadata: Map[String, String] = Map.empty): Task[Product]

  /**
    * プロダクト情報を取得。
    *
    * @see https://pay.jp/docs/api/#%E3%83%97%E3%83%AD%E3%83%80%E3%82%AF%E3%83%88%E6%83%85%E5%A0%B1%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param productId プロダクトID
    * @return プロダクト
    */
  def getProduct(productId: ProductId): Task[Product]

  /**
    * プロダクトを更新。
    *
    * @see https://pay.jp/docs/api/#%E3%83%97%E3%83%AD%E3%83%80%E3%82%AF%E3%83%88%E3%82%92%E6%9B%B4%E6%96%B0
    *
    * @param productId プロダクトID
    * @param invalidAfterOpt このプロダクトの有効日時
    * @param metadata メタデータ
    * @return プロダクト
    */
  def updateProduct(productId: ProductId,
                    invalidAfterOpt: Option[ZonedDateTime],
                    metadata: Map[String, String]): Task[Product]

  /**
    * プロダクトを削除。
    *
    * @see https://pay.jp/docs/api/#%E3%83%97%E3%83%AD%E3%83%80%E3%82%AF%E3%83%88%E3%82%92%E5%89%8A%E9%99%A4
    *
    * @param productId プロダクトID
    * @return 削除結果
    */
  def deleteProduct(productId: ProductId): Task[Deleted[ProductId]]

  /**
    * プロダクトリストを取得。
    *
    * @see https://pay.jp/docs/api/#%E3%83%97%E3%83%AD%E3%83%80%E3%82%AF%E3%83%88%E3%83%AA%E3%82%B9%E3%83%88%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param limitOpt 取得するデータ数の最大値(1~100まで)
    * @param offsetOpt 基準点からのデータ取得を行う開始位置
    * @param sinceOpt ここに指定したタイムスタンプ以降に作成されたデータを取得
    * @param untilOpt ここに指定したタイムスタンプ以前に作成されたデータを取得
    * @return プロダクトのリスト
    */
  def getProducts(limitOpt: Option[Int] = None,
                  offsetOpt: Option[Int] = None,
                  sinceOpt: Option[ZonedDateTime] = None,
                  untilOpt: Option[ZonedDateTime] = None): Task[Collection[Product]]

  // --- Transfer API

  /**
    * 入金情報を取得
    *
    * @see https://pay.jp/docs/api/#transfer-%E5%85%A5%E9%87%91
    *
    * @param transferId 入金ID
    * @return 入金情報
    */
  def getTransfer(transferId: TransferId): Task[Transfer]

  /**
    * 入金リストを取得。
    *
    * @see https://pay.jp/docs/api/#%E5%85%A5%E9%87%91%E3%83%AA%E3%82%B9%E3%83%88%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param statusOpt ステータス
    * @param limitOpt 取得するデータ数の最大値(1~100まで)
    * @param offsetOpt 基準点からのデータ取得を行う開始位置
    * @param sinceOpt ここに指定したタイムスタンプ以降に作成されたデータを取得
    * @param untilOpt ここに指定したタイムスタンプ以前に作成されたデータを取得
    * @return 入金リスト
    */
  def getTransfers(statusOpt: Option[TransferStatusType] = None,
                   limitOpt: Option[Int] = None,
                   offsetOpt: Option[Int] = None,
                   sinceOpt: Option[ZonedDateTime] = None,
                   untilOpt: Option[ZonedDateTime] = None): Task[Collection[Transfer]]

  // --- Event API

  def getEvent(eventId: EventId): Task[Event]

  def getEvents(resourceIdOpt: Option[String] = None,
                objectOpt: Option[String] = None,
                typeOpt: Option[String] = None,
                limitOpt: Option[Int] = None,
                offsetOpt: Option[Int] = None,
                sinceOpt: Option[ZonedDateTime] = None,
                untilOpt: Option[ZonedDateTime] = None): Task[Collection[Event]]

  // --- Token API

  /**
    * テスト用トークンを作成。
    *
    * @see https://pay.jp/docs/api/#token-%E3%83%88%E3%83%BC%E3%82%AF%E3%83%B3
    *
    * @param number カード番号
    * @param expMonth 有効期限(月)
    * @param expYear 有効期限(月)
    * @param cvcOpt CVC
    * @param addressStateOpt 都道府県
    * @param addressCityOpt 市区町村
    * @param addressLine1Opt 番地など
    * @param addressLine2Opt 建物名など
    * @param addressZipOpt 郵便番号
    * @param countryOpt　2桁のISOコード(e.g. JP)
    * @param nameOpt カード保有者名(e.g. “YUI ARAGAKI”)
    * @return トークン
    */
  def createTestToken(number: String,
                      expMonth: Int,
                      expYear: Int,
                      cvcOpt: Option[String] = None,
                      addressStateOpt: Option[String] = None,
                      addressCityOpt: Option[String] = None,
                      addressLine1Opt: Option[String] = None,
                      addressLine2Opt: Option[String] = None,
                      addressZipOpt: Option[String] = None,
                      countryOpt: Option[String] = None,
                      nameOpt: Option[String] = None): Task[Token]

  /**
    * テスト用トークン情報を取得。
    *
    * @see https://pay.jp/docs/api/#%E3%83%88%E3%83%BC%E3%82%AF%E3%83%B3%E6%83%85%E5%A0%B1%E3%82%92%E5%8F%96%E5%BE%97
    *
    * @param tokenId トークンID
    * @return トークン
    */
  def getTestToken(tokenId: TokenId): Task[Token]
}
