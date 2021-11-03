package be.ctnr.mongodeneme.masterchef.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("recipes")
data class Recipe(
    val anasayfa: String,
    val anasayfa_class_adi: String,
    val anasayfa_dikey_foto: String,
    val baslik: String,
    val baslik_orig: String,
    val detay: String,
    val durum: String,
    val eski_id: Any?,
    val guncelleme_tarihi: String,
    @Id
    val id: String,
    val kategori_id: String,
    val kayit_tarihi: String,
    val kname: String,
    val kslug: String,
    val okunma: Any?,
    val resim: String,
    val seo_baslik: String,
    val slug: String,
    val spot: String,
    val tip: String,
    val tip_deger: String,
    val video_suresi: String,
    val yayinlanma_tarihi: String,
    val yerlesim_id: String,
    var date:Date?
)