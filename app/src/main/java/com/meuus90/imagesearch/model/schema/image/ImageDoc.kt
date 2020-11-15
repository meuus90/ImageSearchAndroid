package com.meuus90.imagesearch.model.schema.image

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.meuus90.imagesearch.model.schema.BaseSchema
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Images")
data class ImageDoc(
    @PrimaryKey(autoGenerate = true)
    val databaseId: Int,

    @field:ColumnInfo(name = "collection") val collection: String,
    @field:ColumnInfo(name = "thumbnail_url") val thumbnail_url: String,
    @field:ColumnInfo(name = "image_url") val image_url: String,
    @field:ColumnInfo(name = "width") val width: Int,
    @field:ColumnInfo(name = "height") val height: Int,
    @field:ColumnInfo(name = "display_sitename") val display_sitename: String,
    @field:ColumnInfo(name = "doc_url") val doc_url: String,
    @field:ColumnInfo(name = "datetime") val datetime: String
) : BaseSchema(), Parcelable