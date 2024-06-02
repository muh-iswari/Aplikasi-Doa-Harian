DOKUMENTASI APLIKASI DOA HARIAN



**DESKRIPSI APLIKASI **

Doa Harian adalah sebuah aplikasi yang dirancang untuk menghimpun dan menyajikan berbagai doa harian yang sering dibaca oleh umat Islam. Aplikasi ini menawarkan beberapa doa, mulai dari doa-doa sehari-hari, doa sebelum dan sesudah beraktivitas, hingga doa-doa khusus untuk situasi tertentu. Dengan antarmuka yang user-friendly, aplikasi ini mudah digunakan oleh semua kalangan, membantu kita mengingat dan memanjatkan doa dengan lebih mudah, serta mendekatkan diri kepada Allah SWT dalam setiap langkah kehidupan kita.

Diharapkan Doa Harian dapat memberikan manfaat besar bagi umat Islam, baik yang muda maupun yang tua. Dengan menyajikan berbagai doa yang sering dibaca, aplikasi ini menjadi alat yang sangat berguna dalam mencapai kebaikan dan keberkahan dalam kehidupan sehari-hari. Semoga dengan adanya aplikasi Doa Harian, kita semua bisa lebih istiqamah dalam beribadah dan semakin mendekatkan diri kepada Sang Pencipta.


**CARA PENGGUNAAN **

Sama seperti aplikasi lain yang menampilkan daftar berupa list, aplikasi Doa Harian, sangat mudah digunakan. 
- Kita hanya perlu menghubungkan handphone kita dengan interbet.
- Setelah itu membuka aplikasi Doa Harian untuk selanjutnya mencari doa yang diinginkan, pencarian dapat dilakukan manual maupun dengan menggunakan fitur pencarian.
- Setelah kita menemukan doa yang diinginkan kita sudah dapat membaca doa tersebut. Kita juga dapat menyimpan doa yang diinginkan ke Favorite.
- Kita juga dapat membuka Fragment About untuk melihat deskripsi singkat aplikasi Doa Harian.


**IMPLEMENTASI TEKNIS**

- Activity :
  Terdapat dua activity, yakni activity main dan activity detail.
- Intent :
  Melakukan intent saat melakukan pengambilan data dari API.
- RecyclerView :
  Digunakan saat menampilkan list doa.
- Fragment & Navigation :
  Menggunakan 3 Fragment yaitu HomeFragment, FavoriteFragment, dan AboutFragment. Navigation Component juga digunakan untuk menavigasi perpindahan fragment.
- Background Thread :
  Background Thread digunakan saat melakukan pengambilan data dari API dengan menampilkan progress bar.
- Networking
  API diambil melalui https://doa-doa-api-ahmadramadhan.fly.dev/ . API berisi daftar doa.
- Local Data Persistent
  Local Data Persistent yang digunakan adalah SharePreferences. SharePreferences digunakan untuk menyimpan doa yang ditambahkan ke daftar favorit.
