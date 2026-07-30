# CLAUDE.md

> For agent-scanner metadata see [AGENTS.md](AGENTS.md).

This file provides guidance to Claude Code (claude.ai/code) and other AI agents when working with code in this repository.

## Project Overview

**TIBCO BusinessEvents® Contribution** (`be-contribution`) is a collection of *pluggable, drop-in
implementations* for TIBCO BusinessEvents (BE) — a complex event processing (CEP) platform. Each
contribution is built into a self-contained "uber" jar that is copied into a BE installation's
classpath (`BE_HOME/lib/ext/tpcl/contrib`) and then selected/configured from a BE application. The
repo does **not** contain the BE engine itself — it depends on BE jars provided by a local BE
installation (`be.home`) at build time via Maven `system` scope.

Contributions fall into four categories, each mapping to a BE extension point (SPI):

| Category | What it plugs into | BE extension point |
|----------|--------------------|--------------------|
| **channel** | Inbound/outbound messaging drivers | Custom Channel SPI (`drivers.xml`) |
| **catalog** | BE rule/function "catalog functions" | `@BEPackage` / `@BEFunction` annotations |
| **metric**  | Application metric stores | `MetricsStoreProvider` (`metrics-store.xml`) |
| **store**   | Object persistence / backing stores | `BaseStoreProvider` (`store.xml`) |

The companion BE engine source (the base framework these plugins target) lives separately at
`/Users/vpatil/Work/git/be/trunk` and is the authority for all SPI base types referenced below.
BE product documentation: https://docs.tibco.com/pub/businessevents-enterprise/6.4.0/doc/html/Default.htm

## Prerequisites

- **JDK** — modules compile at `source/target = 11` (root `pom.xml`). The repo README recommends
  Java 17. Building with a newer JDK works (validated with JDK 25); only deprecation warnings appear.
- **Maven** 3.9.x (README recommends 3.9.6; validated with 3.9.11).
- **A TIBCO BusinessEvents installation** (6.3.x or above). Its path is `be.home`. Default
  `be.version` is `6.3.2`. The build wires BE jars from `${be.home}/lib` and `${be.home}/lib/ext`
  as Maven `system`-scope dependencies — **the build cannot resolve them without a real BE install.**
- **Docker** — required to *run* the test suites (all tests are Testcontainers integration tests).
- **External product installs** for four modules only (see [Modules](#modules)): TIBCO FTL, TERR,
  and StreamBase/LiveView. The other nine modules need only `be.home`.

## Build Commands

`be.home` must be set — either edit `<be.home>` in the root `pom.xml` or pass `-Dbe.home=...`.

```bash
# Build every module that needs only be.home (skip Docker-gated tests):
mvn clean install -DskipTests -Dbe.home=/path/to/be/home \
  -pl '!channel/sb,!catalog/ftl-catalog-fn,!catalog/analytics-catalog-fn,!metric/liveview'

# Build a single contribution (recommended workflow — cd into the module):
cd metric/elasticsearch
mvn clean install -Dbe.home=/path/to/be/home -DskipTests

# Build the four modules that also need an external product home:
cd catalog/ftl-catalog-fn      && mvn clean install -Dbe.home=/path/to/be -Dftl.home=/path/to/ftl  -DskipTests
cd catalog/analytics-catalog-fn && mvn clean install -Dbe.home=/path/to/be -Dterr.home=/path/to/terr -DskipTests
cd channel/sb                  && mvn clean install -Dbe.home=/path/to/be -Dsb.home=/path/to/sb    -DskipTests
cd metric/liveview             && mvn clean install -Dbe.home=/path/to/be -Dsb.home=/path/to/sb    -DskipTests

# Run tests for one module (requires Docker running):
cd store/redis && mvn test -Dbe.home=/path/to/be/home

# Full reactor test run, don't stop on first failure:
mvn test -fae -Dbe.home=/path/to/be/home
```

The output jar lands in `<module>/target/<artifactId>-<version>.jar` (the shaded uber-jar; the
pre-shade jar is `original-*.jar`). Copy the shaded jar into `BE_HOME/lib/ext/tpcl/contrib` to make
the contribution available to BE. Some channel/catalog modules also auto-copy the jar into
`BE_HOME/lib/ext/tpcl` at the `install` phase (see [Packaging & drop-in](#packaging--drop-in)).

## Repository Layout

```
be-contribution/
├── pom.xml                     # parent reactor: com.tibco.businessevents:contribution:1.0.0 (packaging pom)
│                               #   - dependencyManagement for all BE system-scope jars + test libs
│                               #   - maven-shade-plugin config (uber-jar + exclude list)
├── channel/     (channel:1.0)  # Custom Channel drivers
│   ├── aws-sqs/                #   AWS SQS               -> aws-sqs-1.0.0.jar
│   ├── kafka-streams/          #   Kafka Streams         -> kafka-streams-1.0.0.jar   (adds Confluent repo)
│   ├── kinesis/                #   Amazon Kinesis        -> kinesis-1.0.0.jar
│   ├── mqtt/                   #   MQTT                  -> mqtt-1.0.0.jar
│   └── sb/                     #   TIBCO StreamBase      -> sb-1.0.0.jar        [needs sb.home]
├── catalog/     (catalog:1.0)  # Catalog (rule) functions
│   ├── aws-catalog-fn/         #   AWS S3/SNS/SQS fns    -> aws-catalog-fn-1.0.0.jar  (adds Shibboleth repo)
│   ├── cassandra-catalog-fn/   #   Cassandra store+fns   -> cassandra-catalog-fn-1.0.0.jar
│   ├── ftl-catalog-fn/         #   TIBCO FTL message fns -> ftl-catalog-fn-1.0.0.jar  [needs ftl.home]
│   └── analytics-catalog-fn/   #   PMML/Statistica/TERR  -> analytics-catalog-fn-1.0.0.jar [needs terr.home]
├── metric/      (metric:1.0)   # Application metric stores
│   ├── elasticsearch/          #   Elasticsearch         -> elasticsearch-1.0.0.jar
│   └── liveview/               #   TIBCO LiveView (LDM)  -> liveview-1.0.0.jar  [needs sb.home]
├── store/       (store:1.0)    # Object persistence / backing stores
│   ├── mongoDB/                #   MongoDB               -> mongoDB-1.0.0.jar
│   └── redis/                  #   Redis + RediSearch    -> redis-1.0.jar   (note: -1.0, not -1.0.0)
├── showcases/                  # Hugo static-site SOURCE for the contributions showcase website (not a build module)
├── docs/                       # Rendered Hugo HTML output of the showcase site (not a build module)
├── README.md, CONTRIBUTING.md, LICENSE
```

The four category poms (`channel`, `catalog`, `metric`, `store`) are thin aggregators — no
properties, no dependencies, no build config of their own.

> Note: some `channel/*` and `catalog/*` module dirs may contain a stray `C:` directory — an
> accidental Windows-path artifact from a prior build. It is not source and can be ignored/removed.

## Modules

Every module produces one shaded jar. "External home" = a product install beyond `be.home` that the
module's pom references via a `system`-scope `systemPath`; those four modules cannot build without it.

### channel — Custom Channel drivers
| Module | Base package | External home | Key third-party deps |
|--------|--------------|---------------|----------------------|
| aws-sqs | `com.tibco.be.custom.channel.aws.sqs` | — | AWS SDK v1 (sqs/sns/s3/sts/core) 1.12.780, OpenSAML 4.3.2, jsoup 1.18.1, bcprov-jdk18on 1.78.1 |
| kafka-streams | `com.tibco.cep.driver.kafkastreams` | — (uses BE `cep-kafka.jar`) | kafka-clients/streams 3.7.2, rocksdbjni, Confluent schema-registry client 7.7.1 |
| kinesis | `com.tibco.be.custom.channel.kinesis` | — | amazon-kinesis-client 1.14.10, aws-java-sdk 1.12.788 |
| mqtt | `com.tibco.cep.driver.mqtt` | — (uses BE HTTP driver `SSLUtils`) | Eclipse Paho mqttv3 1.2.5, jackson 2.17.2 |
| sb | `com.tibco.cep.driver.sb` | **StreamBase** (`sb.home` → `lib/sb-java-tools.jar`) | antlr stringtemplate 3.2 |

Common shape: a `*Driver` (entry), `*Channel` (lifecycle), `*Destination` (send/consume), and one or
more `serializer.*` classes converting the wire payload ↔ BE events. SQS/Kinesis add credential
strategies (default chain, access/secret key, assume-role/STS, SAML). Only **aws-sqs** ships tests.

### catalog — Catalog (rule) functions
| Module | Base package | External home | Notes |
|--------|--------------|---------------|-------|
| aws-catalog-fn | `com.tibco.be.custom.aws.services.*` | — | `AWS.{s3.Bucket, sns.Notification, sqs.Queue}` functions; 4 auth modes each (OpenSAML 4.3.2); has unit+integration tests |
| cassandra-catalog-fn | `com.tibco.cep.store.cassandra.*` | — | *Both* a `store` SPI provider **and** `CEP Store` catalog functions; Datastax driver 4.17.0 (pre-shaded) |
| ftl-catalog-fn | `com.tibco.cep.functions.channel.ftl` | **FTL** (`ftl.home` → `lib/tibftl.jar`) | Single class `MessageHelper`: `FTL.Message` get/set/clear/destroy |
| analytics-catalog-fn | `com.tibco.cep.analytics.*` | **TERR** (`terr.home` → `library/terrJava/java/terrJava.jar`) | `Analytics.{PMML, Statistica, TERR}` catalogs; jpmml 1.5.16, saaj 1.5.3 |

Catalog functions are plain classes annotated with `@com.tibco.be.model.functions.BEPackage` (class)
and `@BEFunction` (methods); BE discovers them by annotation scanning at engine start.

### metric — Application metric stores
| Module | Base package | External home | Notes |
|--------|--------------|---------------|-------|
| elasticsearch | `com.tibco.metric.store.elasticsearch` | — | ES REST high-level client 7.17.28; publishes entity data to ES on RTC completion; has tests |
| liveview | `com.tibco.cep.liveview` | **StreamBase/LiveView** (`sb.home` → `lib/sbclient.jar`, `lib/lv-client.jar`) | Publishes concepts/events/scorecards to a LiveView (Live DataMart) server |

Each provides a `*MetricsStoreProvider` (implements `MetricsStoreProvider<T>`) and a
`*MetricsRecordBuilder` (implements `MetricsRecordBuilder<T>`). BE loads the provider named in the
application's `metrics-store.xml` via `Class.forName`.

### store — Object persistence / backing stores
| Module | Base package | External home | Notes |
|--------|--------------|---------------|-------|
| mongoDB | `com.tibco.be.mongoDB` | — | MongoDB sync driver 4.11.5; has tests |
| redis | `com.tibco.be.redis` | — | Lettuce 6.2.6 + LettuSearch 2.4.4 (RediSearch 2.0); has tests |

Each provides a `*StoreProvider` (extends `BaseStoreProvider`), a `*LockProvider` (extends
`AbstractLockProvider` / implements `ILockProvider`), a `*DataTypeMapper`, and an aggregation
builder. BE selects the provider named in the application's `store.xml` (via `StoreHelper`).

## Extension Model — how a contribution hooks into BE

All plugin base types below were verified to exist in the BE engine source
(`/Users/vpatil/Work/git/be/trunk`), and every overridden method matches the base signature.

- **Channels** extend the Custom Channel SPI base classes
  (`runtime/modules/channels/custom/.../be/custom/channel/`: `BaseDriver`/`BaseChannel`/
  `BaseDestination`/`BaseEventSerializer`). The `sb` channel instead extends the *native* channel SPI
  (`runtime/core/common/.../runtime/channel/`: `spi/ChannelDriver`, `impl/AbstractChannel`,
  `impl/AbstractDestination`). BE discovers drivers via a `drivers.xml` on the classpath
  (`DriverManager.getResources("drivers.xml")`); the driver "type" string is chosen in Studio's New
  Channel wizard.
- **Catalog functions** use annotations only (`@BEPackage` + `@BEFunction`, package
  `runtime/core/common/.../be/model/functions/`), discovered by BE's annotation processor. No base
  class or descriptor file is required.
- **Metric stores** implement `MetricsStoreProvider<T>` / `MetricsRecordBuilder<T>`
  (`runtime/core/common/.../runtime/appmetrics/`), named in `metrics-store.xml`.
- **Backing/object stores** extend `BaseStoreProvider` + lock/type-mapper base types
  (`runtime/modules/store/.../store/custom/` and `.../store/locking/`), named in `store.xml`. The
  `cassandra-catalog-fn` store additionally registers an `IStoreFactory` via the Java **ServiceLoader**
  (`META-INF/services/com.tibco.cep.store.factory.IStoreFactory`).
- **Descriptor discovery for metric/store** also scans `${BE_HOME}/lib/ext/tpcl/contrib`
  (`AbstractConfigManager`), which is why the drop-in target is that `contrib` directory.

Two channels depend on *sibling BE modules* being on the classpath (they are, in any BE install):
`kafka-streams` imports BE's built-in Kafka driver classes (`com.tibco.cep.driver.kafka.*`), and
`mqtt` imports BE's HTTP driver `SSLUtils`.

## Packaging & Drop-in

Contributions are packaged with **maven-shade-plugin** into an uber-jar so third-party dependencies
travel with the plugin. The root `pom.xml` excludes jars that BE already ships (to avoid duplicate/
conflicting classes on the BE classpath):

- `com.tibco.*:cep*` (BE core), `jackson-core`, `jackson-dataformat-cbor`, `log4j-api`,
  `httpclient`, `httpcore`, `httpcore-nio`, `commons-codec`, `commons-logging`.

**Verified drop-in status (built jars inspected):** no BE core (`com.tibco.cep.*` framework)
classes leak into any jar — the only `com/tibco/...` entries are each plugin's own code.
`mongoDB` is the cleanest drop-in. The remaining risk is duplicate **third-party** libraries, not BE
classes.

The root shade config now (a) excludes the full set of BE-provided libraries — `com.tibco.*:cep*`,
all core `jackson` (core/databind/annotations/cbor), `log4j-api`+`log4j-core`, the httpclient stack
(`httpclient`/`httpcore`/`httpcore-nio`/`httpasyncclient`/`commons-httpclient`), `commons-codec`,
`commons-logging`, `com.google.guava:guava`, `io.netty:*`, and `slf4j-api`; and (b) strips bundled
JAR signatures (`META-INF/*.SF/*.DSA/*.RSA`) so repackaged signed deps (e.g. BouncyCastle) don't
throw `SecurityException` at class-load. The previously-divergent per-module shade overrides in the
channel/catalog modules were **removed** so every module inherits this one exclude list — verify with
`jar tf target/<artifact>.jar | grep -E 'com/fasterxml/jackson|io/netty|com/google/common|org/apache/logging/log4j'`
(should be empty; a 2-class `com.google.guava:failureaccess` shim may remain — no CVE).

⚠️ **Remaining packaging notes — read before shipping a jar:**

1. **redis uber-jar is not directly usable.** Its README requires copying `lettusearch-2.4.4.jar`
   *before* `lettuce-core-*.jar` on `tibco.env.STD_EXT_CP` in `be-engine.tra` (class ordering).
   Netty is now excluded from the jar (provided by BE / pinned via the root `netty-bom`).
2. **elasticsearch** needs `commons-codec` ≥ 1.11 under `BE_HOME/lib/ext/tpcl/apache` for
   username/password auth (documented known issue in its README).
3. **Copy target vs discovery mismatch.** Channel/catalog module poms copy the shaded jar to
   `${be.home}/lib/ext/tpcl` at `install`, but metric/store descriptor discovery scans
   `.../lib/ext/tpcl/contrib`. The documented manual drop-in target is `.../tpcl/contrib` — reconcile
   when relying on auto-copy for metric/store jars.

## Testing

- All test suites are **Testcontainers-based integration tests** (JUnit 5 Jupiter + Mockito 3.7 +
  Testcontainers 1.15.1). Even classes named `*UnitTest` (in `aws-catalog-fn`) are `@Testcontainers`
  with a static `@Container`, so **they require a running Docker daemon.** Without Docker they fail at
  class initialization with `Could not find a valid Docker environment` — this is an environment
  limitation, not a code defect. Test *sources compile and execute* up to container startup.
- Modules with tests: `channel/aws-sqs` (localstack), `catalog/aws-catalog-fn` (localstack),
  `catalog/cassandra-catalog-fn` (cassandra), `metric/elasticsearch` (elasticsearch),
  `store/mongoDB` (mongodb), `store/redis` (redis). The remaining modules have no `src/test`.
- Run with Docker available: `mvn test -Dbe.home=/path/to/be/home` (per module), or add `-fae` at the
  reactor root to test every module regardless of earlier failures.

## Conventions & Gotchas

- **`be.home` is mandatory** for any build (BE jars are `system` scope). The default `<be.home>` in
  the root pom is a placeholder (`/path/to/be/home`).
- **Four modules need external product homes** and will fail dependency resolution without them:
  `ftl-catalog-fn` (`ftl.home`), `analytics-catalog-fn` (`terr.home`), `sb` and `liveview`
  (`sb.home`). Exclude them from a `be.home`-only reactor build with
  `-pl '!channel/sb,!catalog/ftl-catalog-fn,!catalog/analytics-catalog-fn,!metric/liveview'`.
- **redis artifact version is `1.0`**, not `1.0.0` — its module pom omits `<version>` so it inherits
  `store:1.0`. Jar is `redis-1.0.jar`; all other modules are `-1.0.0.jar`.
- **kinesis** copies its jar to `lib/ext/tpcl/contrib` at install; other channel/catalog modules copy
  to `lib/ext/tpcl`; metric/store modules do not auto-copy.
- **Extra Maven repos:** `kafka-streams` adds the Confluent repo; `aws-catalog-fn` and `aws-sqs` add
  the Shibboleth repo (for OpenSAML 4.3.2).
- **Stale reference:** `metric/elasticsearch/src/main/resources/metrics-store.xml` has a comment
  naming `com.tibco.cep.runtime.service.cluster.metric.MetricsStore` (a package that does not exist in
  the engine); the actually-implemented interface is `com.tibco.cep.runtime.appmetrics.MetricsStoreProvider`.
  Harmless comment only.

## Adding a New Contribution

1. Create a Maven module under the appropriate category dir (`channel`/`catalog`/`metric`/`store`) and
   register it in that category's `pom.xml` `<modules>`.
2. Set the parent to the category pom; declare only *contribution-specific* dependencies (BE jars and
   common test libs come from the root `dependencyManagement`).
3. Implement against the correct BE SPI (see [Extension Model](#extension-model--how-a-contribution-hooks-into-be))
   and add the required descriptor/annotation (`drivers.xml`, `@BEPackage`, `metrics-store.xml`, or
   `store.xml`).
4. Keep the uber-jar lean: exclude everything BE already ships (extend the parent shade excludes
   rather than overriding the `artifactSet` — see [Packaging pitfalls](#packaging--drop-in)).
5. Add Testcontainers integration tests and a module `README.md` documenting config/usage.
6. Build with `-Dbe.home=...`, then copy the shaded jar to `BE_HOME/lib/ext/tpcl/contrib` and verify
   in a real BE application.

## Validation Status (last checked 2026-07-29, BE 6.3.2)

- **Compiles:** all 13 modules build and produce shaded jars cleanly (JDK 25, Maven 3.9.11). The 9
  `be.home`-only modules and the 4 external-home modules (`sb`, `liveview`, `ftl-catalog-fn`,
  `analytics-catalog-fn`) were each built and verified. Local homes used on this machine:
  `be.home=.../632_GA_HF1/be/6.3`, `ftl.home=.../tibco/ftl/7.0.0/V11-GA/macosx/x86_64`,
  `sb.home=.../tibco/str/11.0.1/V3-GA/macosx/x86_64`,
  `terr.home=.../tibco/terr/6.0.2/V13-GA/macosx/x86_64`.
- **SPI alignment:** all 13 modules align with BE core SPIs — every extended/implemented base type
  exists in `be/trunk` and every override matches the base signature. No SPI-level runtime breaks.
- **Drop-in:** no BE core classes leak into any jar. Third-party duplication is the only risk; see
  [Packaging & drop-in](#packaging--drop-in). `mongoDB` is a clean drop-in as built.
- **Tests:** not executed here — Docker daemon unavailable in the validation environment. Re-run with
  Docker to exercise the integration suites.

## Dependency & Vulnerability Remediation

A Dependabot remediation pass bumped vulnerable third-party dependencies and repaired the packaging
so the fixes ship in the drop-in jars. Strategy:

- **Root `dependencyManagement` pins** (affect all modules, incl. transitive deps): `jackson-bom`
  2.17.2, `netty-bom` 4.1.115.Final, `aws-java-sdk-bom` 1.12.788, `guava` 33.3.1-jre.
- **Direct bumps by module** (highlights): AWS SDK v1 → 1.12.780/1.12.788; `amazon-kinesis-client`
  → 1.14.10 (kept on v1 API); Kafka clients/streams → 3.7.2 + Confluent → 7.7.1; jackson → 2.17.2;
  DataStax driver → 4.17.0; Elasticsearch RHLC → 7.17.28 (import fix: `TimeValue` →
  `org.elasticsearch.core`); MongoDB driver → 4.11.5; Lettuce → 6.2.6 + reactor → 3.4.34 +
  commons-pool2 → 2.12.0; `bcprov-jdk15on` 1.67 → `bcprov-jdk18on` 1.78.1 (old 1.51 excluded from the
  opensaml/xmltooling subtree in both AWS modules); jsoup → 1.18.1; commons-io → 2.17.0; commons-lang3
  → 3.18.0.
- **Packaging:** per-module shade overrides removed; root shade excludes extended + signature-strip
  filter added (see [Packaging & Drop-in](#packaging--drop-in)).

**Code migrations completed (compile-verified; require runtime validation before release):**
1. **OpenSAML 2.6.4 (EOL) → 4.3.2** in `aws-sqs` + `aws-catalog-fn` (Java-11 compatible; no JDK bump).
   Removed `opensaml:2.6.4` + `xmltooling:1.4.6`; added the seven `opensaml-*:4.3.2` artifacts
   (java-support 8.4.2 transitive). Rewrote `saml2/SAMLService` (`DefaultBootstrap`→
   `InitializationService`, v4 `org.opensaml.saml.saml2.*` + `core.xml` packages, initialized
   `net.shibboleth...BasicParserPool` parsing an `InputStream`) and `saml2/IdpMetadataService`
   (`FilesystemMetadataProvider`→`FilesystemMetadataResolver` with `CriteriaSet`/`EntityIdCriterion`).
   Added the Shibboleth repo to aws-sqs; restored `commons-lang:2.6` in aws-catalog-fn (v2 used to
   supply it transitively for `s3/Bucket.java`). ⚠️ **Not runtime-tested** — a reviewer must validate
   `parseSAMLResponse`, metadata resolution, and the end-to-end `AssumeRoleWithSAML` STS flow against a
   real IdP (ADFS/PingFederate/Shibboleth).
2. **`org.jpmml:pmml-evaluator-extension` 1.4.15 → 1.5.16** in `analytics-catalog-fn` (newest release
   that keeps `org.dmg.pmml.FieldName`; 1.6.x removes it, which would force a large signature rewrite).
   Fixed `pmml/io/PmmlFunctionsDelegate.java`: `ModelEvaluatorFactory.newModelEvaluator(pmml)` →
   `new ModelEvaluatorBuilder(pmml).build()`; `ValueOptimizer`/`NodeScoreOptimizer` →
   `ValueParser`/`NodeScoreParser`. ⚠️ **Not runtime-tested** — a reviewer must score a real PMML model
   and confirm parity with 1.4.15. (Pre-existing quirk preserved: optimizer visitors run *after* the
   evaluator is built, so they don't affect the cached evaluator.)
