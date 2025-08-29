# KanaConverter
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![Unlicense License][license-shield]][license-url]
[![Maven Central Version][central-shield]][central-url]
[![App Version][version-shield]][header-id]

## Overview
This is a library that converts Roman letters to both hiragana and katakana and vice versa.<br/>

## Installation
### Maven
```xml
<dependency>
    <groupId>com.shotadft</groupId>
    <artifactId>kana-converter</artifactId>
    <version>1.1.2</version>
</dependency>
```
### Gradle(Groovy DSL)
```groovy
dependencies {
    implementation 'com.shotadft:kana-converter:1.1.2'
}
```
### Gradle(Kotlin DSL)
```kts
dependencies {
    implementation("com.shotadft:kana-converter:1.1.2")
}
```

## Requirement
- Java (21)
- Kotlin (2.2.10)

## Development Environments
- IntelliJ IDEA Community Edition (2025.2)
- Gradle (8.9)
  - dokka (2.0.0)
  - spotless (7.2.1)
- Eclipse Temurin JDK (21.0.8+9)

## Usage
```kotlin
import com.shotadft.kanaconverter.KanaConverter.toHiragana
import com.shotadft.kanaconverter.KanaConverter.toKatakana
import com.shotadft.kanaconverter.KanaConverter.toRomaji

fun main() {
    val hiragana = "こんにちは"
    val katakana = "コンニチハ"
    val romaji = "kon'nichiha"

    println(hiragana.toKatakana()) // コンニチハ
    
    println(hiragana.toRomaji())   // kon'nichiha
    println(katakana.toRomaji())   // kon'nichiha
    
    println(romaji.toHiragana())   // こんにちは
    println(romaji.toKatakana())   // コンニチハ
}
```

## Features
- 高速、そしてまあまあ高い精度で変換できます。<br>対応してないものがあるかもしれないけどその場合はIssueを立ててね<br>
(ただし、ヘボンと訓令を両対応させる都合でtoRomajiの精度がかなり低いです...)
- fastutilのマップを簡単に構築できるツールを使用してメモリ消費量を抑えています。<br>また、キャッシュによって変換速度を向上させています。
- IssueやPull Requestは大歓迎です。<br>バグ報告、機能追加、コード改善など何でもどうぞ。

## Author Links
- [Twitter](https://x.com/shotadft)
- [My HP](https://www.shotadft.com/)

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[forks-shield]: https://img.shields.io/github/forks/shotadft/KanaConverter.svg?style=for-the-badge
[forks-url]: https://github.com/shotadft/KanaConverter/network/members
[stars-shield]: https://img.shields.io/github/stars/shotadft/KanaConverter.svg?style=for-the-badge
[stars-url]: https://github.com/shotadft/KanaConverter/stargazers
[issues-shield]: https://img.shields.io/github/issues/shotadft/KanaConverter.svg?style=for-the-badge
[issues-url]: https://github.com/shotadft/KanaConverter/issues
[license-shield]: https://img.shields.io/github/license/shotadft/KanaConverter.svg?style=for-the-badge
[license-url]: https://github.com/shotadft/KanaConverter/blob/master/LICENSE.md
[central-shield]: https://img.shields.io/maven-central/v/com.shotadft/kana-converter?style=for-the-badge
[central-url]: https://central.sonatype.com/artifact/com.shotadft/kana-converter
[version-shield]: https://img.shields.io/badge/1.1.2-00c81b?label=version&style=for-the-badge
[header-id]: #KanaConverter
