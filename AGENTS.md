# AGENTS.md — TIBCO BusinessEvents® Contribution

> Detailed project context, build instructions, module reference, SPI/extension model, packaging
> pitfalls, and conventions are in [CLAUDE.md](CLAUDE.md). Read it for anything beyond this overview.

## Repository

- **Name:** TIBCO BusinessEvents Contribution (`be-contribution`)
- **Purpose:** Pluggable, drop-in extensions for the TIBCO BusinessEvents (BE) CEP engine — channels,
  catalog functions, metric stores, and object/backing stores. Not the BE engine itself.
- **Language:** Java (source/target level 11; JDK 17+ recommended)
- **Build:** Maven 3.9.x multi-module reactor (`mvn clean install -Dbe.home=/path/to/be/home`)
- **Requires:** a local BE installation (`be.home`, default `be.version` 6.3.2) — BE jars are wired as
  Maven `system`-scope dependencies. Docker is required to run tests.
- **Companion engine source (SPI authority):** `/Users/vpatil/Work/git/be/trunk`

## Modules

| Category | Path | Contributions | Extension point |
|----------|------|---------------|-----------------|
| Channels | `channel/` | aws-sqs, kafka-streams, kinesis, mqtt, sb* | Custom Channel SPI (`drivers.xml`) |
| Catalog functions | `catalog/` | aws-catalog-fn, cassandra-catalog-fn, ftl-catalog-fn*, analytics-catalog-fn* | `@BEPackage`/`@BEFunction` |
| Metric stores | `metric/` | elasticsearch, liveview* | `MetricsStoreProvider` (`metrics-store.xml`) |
| Object/backing stores | `store/` | mongoDB, redis | `BaseStoreProvider` (`store.xml`) |

\* Needs an external product install beyond `be.home`: `sb`/`liveview` → StreamBase (`sb.home`),
`ftl-catalog-fn` → FTL (`ftl.home`), `analytics-catalog-fn` → TERR (`terr.home`). See CLAUDE.md.

Non-module directories: `showcases/` (Hugo site source) and `docs/` (rendered site output).

## Scanning Notes

- Source is under `src/main/java`; tests under `src/test/java`. Category poms are thin aggregators.
- Each module builds one shaded uber-jar in `<module>/target/`; the drop-in target in a BE install is
  `BE_HOME/lib/ext/tpcl/contrib`.
- All test classes are Testcontainers integration tests and **require a running Docker daemon**
  (including classes named `*UnitTest`).
- When touching packaging, mind the shade `artifactSet` override caveat and the incomplete parent
  exclude list documented in CLAUDE.md → *Packaging & Drop-in* — this is the main source of drop-in
  jar conflicts.
- Ignore any stray `C:` directories inside module folders (Windows-path build artifacts) and
  `target/` output.
