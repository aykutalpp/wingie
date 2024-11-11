project structure da SDK 20 yap
invalidate caches and restart yap
load maven project yap

webdriver klasorunun altina chrome surumun ile uyumlu chromedriver ekle

guncel chromedriver icin asagidaki adresi kullanabilirsin

https://googlechromelabs.github.io/chrome-for-testing/#stable

gauge plugin ide de kurulu olmali

specs/HappyPathSenaryolar/enUygunFligthSearch.spec  dosyasinden test kosumu yapilabilir 

veya terminalden kosum icin

mvn -B -f ./pom.xml test-compile gauge:execute -DspecsDir=specs/HappyPathSenaryolar/enUygunFligthSearch.spec -Dtags=case1
mvn -B -f ./pom.xml test-compile gauge:execute -DspecsDir=specs/HappyPathSenaryolar/enUygunFligthSearch.spec -Dtags=case2

