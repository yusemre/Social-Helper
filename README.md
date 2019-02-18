# Social-Helper
--------------- 
The project was developed for people in need and homeless
people.
- You can share help location.
- You can join the help chat.
- You can post help status.

Keywords : Kotlin, Java, Android, Firebase

--------------- 

Proje android işletim sistemi öncelikli kullanıcıların sisteme kayıt olabildiği ve harita üzerinden yardım konumu belirleyebildiği ayrıca kendi aralarında yardım odası oluşturup bu odada kullanıcıların odanın türüne göre yardım konularını konuşabildiği, tüm kullanıcıların gerçek zamanlı konuşup yardım edecekleri kişiyi daha hızlı belirlemeyi amaçlamaktadır. Çeşitli sosyal medya uygulamaları gibi kullanıcılar diğer kullanıcılar ile resimli yardım postu da paylaşabilmektedir. Uygulamanın harita arayüzünde kullanıcının mevcut konumu alınıp konumuna yakın yerlerindeki yardım noktalarını görebilmektedir ve kullanıcı yeni bir yardım noktası ekleyebilmektedir. Uygulama son olarak kullanıcıya uygulamayı değerlendirme fırsatı sunar böylece gelen geri bildirimlere göre uygulamada iyileştirmeler yapılmıştır.

Uygulama gerçekleştirilirken, Android Studio 3.0 üzerinde Google tarafından yakın zamanda resmi olarak desteklenen kotlin dili kullanılmıştır. Uygulama verilerinin saklanması için gerçek zamanlı olarak depolayabildiğimiz, bulut tabanlı Firebase kullanılmıştır.

## --Veritabanı Tasarımı
Projedeki veritabanının ER diyagramı şu şekildedir;
![social helper er 2](https://user-images.githubusercontent.com/11167289/52975847-264c5500-33d8-11e9-941b-0e1116b8aa57.png)


---------------

Uygulama dahilinde test edilmiş özellikler aşağıda verilmiştir.
- Kullanıcı kayıt olma eylemi
- Kullanıcı giriş eylemi
- Kullanıcı mail onayının gerçekleşmesi
- Kullanıcı profil bilgilerini güncelleyebilme eylemi
- Kullanıcı şifre değiştirme eylemi
- Profil resmi ve konum erişimi için gereken iznin sağlanması
- Resim ekleme sonrası veritabanına sağlıklı kayıt edilip edilmediği
- Gerçek zamanlı verilerin her kullanıcı tarafından görülebilmesi
- Sohbet odası oluşturma eylemi
- Sohbet odası seviye sorgulama eylemi
- Sohbet odasından okunmayan mesajların bildirime düşmesi ve görüntülenmesi eylemi
- Harita arayüzünde kullanıcının mevcut konumunu bulma eylemi
- Eklenen konumun tüm kullanıcılar tarafından görülebilmesi
- Uygulamadaki butonların beklenen fonksiyonları gerçekleştirip gerçekleştirmediği
- Veri tabanından verilerin doğru bir şekilde çekilmesi
- Veritabanı servis kısmından gönderilen bildirimlerin ulaşması
- Uygulamanın arka plana atıldığında kullanıcının sistemden düşmemesi
- Kullanıcının uygulamadan sağlıklı bir şekilde çıkış yapabilmesi

## --Bazı Ekran Görüntüleri
![ekran1](https://user-images.githubusercontent.com/11167289/52976160-6b24bb80-33d9-11e9-9a97-3efab5818316.jpg)![ekran2](https://user-images.githubusercontent.com/11167289/52976196-95767900-33d9-11e9-94dc-3a462dabfaef.jpg)![ekran3](https://user-images.githubusercontent.com/11167289/52976218-ade69380-33d9-11e9-9bf0-73bed9ba5571.jpg)



