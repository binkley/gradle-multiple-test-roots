<a href="LICENSE.md">
<img src="https://unlicense.org/pd-icon.png" alt="Public Domain" align="right"/>
</a>

# Gradle multiple test roots

A demo multi-module project for
[Gradle test sets](https://github.com/unbroken-dome/gradle-testsets-plugin)

[![build](https://github.com/binkley/gradle-multiple-test-roots/workflows/build/badge.svg)](https://github.com/binkley/gradle-multiple-test-roots/actions)
[![issues](https://img.shields.io/github/issues/binkley/gradle-multiple-test-roots.svg)](https://github.com/binkley/gradle-multiple-test-roots/issues/)
[![Public Domain](https://img.shields.io/badge/license-Public%20Domain-blue.svg)](http://unlicense.org/)
## Usage

```
./gradlew --no-build-cache build taskTree
```

After building at least once, you can see the impact of the build cache:

```
./gradlew build taskTree
```

[Gradle task tree](https://github.com/dorongold/gradle-task-tree) pretty
prints the tasks that would run.

## Maintenance

To update Gradle:

```
$ ${EDITOR-vi} gradle.properties
# Edit the Gradle version
$ ./gradlew wrapper
```

## Structure

* `[ab]/src/main/*` - Production code and resources
* `[ab]/src/test/*` - Unit test code and resources
* `[ab]/src/integrationTest/*` - Integration test code and resources

Module `a` depends on module `b`.

## Build features

- All version details externalized to [`gradle.properties`](gradle.properties)
- Java
- Build dashboard &mdash; top-level view of Gradle reports
- JaCoCo &mdash; code coverage
- Task tree &mdash; display task dependencies
- Test sets &mdash; multiple test source roots
- Versions &mdash; update to latest plugin/dependency versions

## TODO

- Something broke with SpotBugs Gradle plugin between Gradle 6.3 and 6.4.1
