package com.genta.storyapp.Data.Response

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
@Entity(tableName = "story")
@Parcelize
data class ResponseGetStory(
    @PrimaryKey
    @SerializedName("id"          ) var id          : String,
    @SerializedName("name"        ) var name        : String? = null,
    @SerializedName("description" ) var description : String? = null,
    @SerializedName("photoUrl"    ) var photoUrl    : String? = null,
    @SerializedName("createdAt"   ) var createdAt   : String? = null,
    @SerializedName("lat"         ) var lat         : Double? = null,
    @SerializedName("lon"         ) var lon         : Double? = null

) : Parcelable


data class MsgGetStory(
    @SerializedName("error"     ) var error     : Boolean,
    @SerializedName("message"   ) var message   : String,
    @SerializedName("listStory" ) var listStory : ArrayList<ResponseGetStory>
)

