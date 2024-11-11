enUygun Cases
======================

## case1
tags:case1
* Go to enuygun.com and check that you are on the homepage
* Search a round-trip flight from "İstanbul, Türkiye - Tüm Havalimanları" to "Ankara, Türkiye ESB - Esenboğa Havalimanı" between "2024-11-24" and "2024-11-30"
* Filter the departure and arrival times between 10:00 and 18:00
* Assert that filtered flight's departure and arrival times are between 10:00 and 18:00
//* Wait for "3" seconds

## case2
tags:case2
* Go to enuygun.com and check that you are on the homepage
* Search a round-trip flight from "İstanbul, Türkiye - Tüm Havalimanları" to "Ankara, Türkiye ESB - Esenboğa Havalimanı" between "2024-11-24" and "2024-11-30"
* Filter the departure and arrival times between 10:00 and 18:00
* Filter the airline as Turkish Airlines
* Assert that filtered flight's departure and arrival times are between 10:00 and 18:00
* Assert that filtered flight price's are in descending order
//* Wait for "3" seconds

## case3
tags:case3
// enuygun.com kullanırken Farkli url lere gidilirse impacti yuksek olacagini dusunuyorum
// bu amacla case1 ve case2 ye sayfa gecislerinde url kontrolu ekledim.
