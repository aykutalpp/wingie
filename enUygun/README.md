specs/HappyPathSenaryolar/enUygunFligthSearch.spec  dosyasinden test kosumu yapilabilir 

veya terminalden kosum icin

mvn -B -f ./pom.xml test-compile gauge:execute -DspecsDir=specs/HappyPathSenaryolar/enUygunFligthSearch.spec -Dtags=case1
mvn -B -f ./pom.xml test-compile gauge:execute -DspecsDir=specs/HappyPathSenaryolar/enUygunFligthSearch.spec -Dtags=case2

