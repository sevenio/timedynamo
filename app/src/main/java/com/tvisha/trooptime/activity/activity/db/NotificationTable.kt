package com.tvisha.trooptime.activity.activity.db

import android.content.ContentValues
import android.content.Context
import org.json.JSONObject

public class NotificationTable(context:Context) : DbContext(context) {
    private val db = database
    companion object {
        const val NOTIFICATION_TABLE = "notification"
        const val  ID = "id"
        const val NOTIFICATION_DATA = "notification_data"
        const val NOTIFICATION_TYPE = "notification_type"
        const val CREATED_AT = "created_at"
        const val UPDATED_AT = "updated_at"
        const val CREATE_NOTIFICATION_TABLE = "CREATE TABLE IF NOT EXISTS $NOTIFICATION_TABLE (" +
                ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                NOTIFICATION_DATA +" TEXT ,"+
                NOTIFICATION_TYPE +" INTEGER DEFAULT 0 ,"+
                CREATED_AT +" TIMESTAMP ,"+
                ID +" TIMESTAMP ,"+
                ")"

    }
    public fun insertNotification(jsonObject: JSONObject){
        var conteValues = ContentValues()
        conteValues.put(NOTIFICATION_DATA,jsonObject.toString())
        conteValues.put(NOTIFICATION_TYPE,jsonObject.optInt("type"))
        conteValues.put(CREATED_AT,jsonObject.optString("created_at"))
        conteValues.put(UPDATED_AT,jsonObject.optString("updated_at"))
        db.insert(NOTIFICATION_TABLE,null,conteValues)
    }
    /*public fun fetchNotifications(type:Int):LiveData<List<Notification>>{
        *//*return  Pager(config = PagingConfig(pageSize = 50, prefetchDistance = 10,enablePlaceholders = false, initialLoadSize = 50, maxSize = 20000)) {
            db.rawQuery("SELECT ID, notification_data as notificationData,notiofication_type as notificationType,created_at,updated_at FROM notification WHERE notification_type = "+type+" order by created_at desc",null)
        }.flow.map{pagingData -> {pagingData.map{cheese->cheese}}.cachedIn(viewModel.viewModelScope).flowOn(Dispatchers.IO)}*//*
    }*/

}