# pay-jp-scala

`pay-jp-scala` is unofficial pay-jp client implemented by Scala.

## Main features

- Merchat API
    - Account
    - Customer
    - Product
    - Plan
    - Charge
    - Subscription
    - Transfer
    - Event
- Platform API
    - PlatformMerchant
    - PlatformTransfer 

## Main library dependency

- akka-http
- akka-stream
- circe
- monix

## Installation

Add the following to your sbt build (Scala 2.12.x):

### Release Version

```scala
resolvers += "Sonatype OSS Release Repository" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies += "com.github.j5ik2o" %% "pay-jp-scala" % "1.0.0"
```

### Snapshot Version

```scala
resolvers += "Sonatype OSS Snapshot Repository" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies += "com.github.j5ik2o" %% "pay-jp-scala" % "1.0.0-SNAPSHOT"
```
    
## Usage

```scala
import monix.execution.Scheduler.Implicits.global
import scala.concurrent.Await

val apiClientContext: ApiClientContext   = new ApiClientContext("api.pay.jp", 443)
val merchantApiClient: MerchantApiClient = apiClientContext.createMerchantApiClient(sys.env("MERCHANT_SECRET_KEY"))
val platformApiClient: PlatformApiClient = apiClientContext.createPlatformApiClient(sys.env("PLATFORM_SECRET_KEY"))

// Charge API
val future = merchantApiClient.createCharge(
  amountAndCurrency = Some((Amount(10000L), Currency("jpy"))), 
  productId = None,
  customerId = Some(user1.id),
  tokenId = None,
  description = None,
  capture = None,
  expiryDays = None,
  metadata = Map.empty,
  platformFee = None).runAsync
  
val result = Await.result(future, 3 seconds)

apiClientContext.sender.shutdown()
```
 

