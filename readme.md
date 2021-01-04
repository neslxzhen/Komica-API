### gradle script

```groovy
// 自動化測試（搭配 JUnit、Spock 等）
gradlew test

// 自動檢查程式碼品質（搭配 CheckStyle 及 CodeNarc）
gradlew check

// 執行 Jetty 網頁伺服器
gradlew jettyRun

// 執行 Tomcat 網頁伺服器
gradlew tomcatRunWar

// 打包成 WAR 檔
gradlew war

// 可以自訂一個自動發佈到遠端伺服器的任務
gradlew deployToServer
```