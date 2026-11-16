package com.wherepaid.app.core.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.wherepaid.app.core.model.LatLng
import com.wherepaid.app.core.model.PaymentKind
import com.wherepaid.app.core.model.Place
import com.wherepaid.app.core.model.Transaction

// 알림 원문(title, text 등)을 담는 컬럼은 만들지 않는다. 파싱 결과만 저장한다.

@Entity(tableName = "transactions", indices = [Index("paidAt"), Index("placeId")])
internal data class TransactionEntity(
    @PrimaryKey val id: String,
    val amount: Long,
    val merchantRaw: String?,
    val cardName: String?,
    val payType: String?,
    /** [PaymentKind.name] */
    val kind: String,
    val paidAt: Long,
    val latitude: Double?,
    val longitude: Double?,
    val locationAccuracyM: Float?,
    val placeId: String?,
    val isOnline: Boolean,
    val isUserEdited: Boolean,
)

@Entity(tableName = "places")
internal data class PlaceEntity(
    @PrimaryKey val id: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val category: String?,
    val address: String?,
)

internal fun TransactionEntity.toModel() = Transaction(
    id = id,
    amount = amount,
    merchantRaw = merchantRaw,
    cardName = cardName,
    payType = payType,
    kind = PaymentKind.valueOf(kind),
    paidAt = paidAt,
    location = if (latitude != null && longitude != null) LatLng(latitude, longitude) else null,
    locationAccuracyM = locationAccuracyM,
    placeId = placeId,
    isOnline = isOnline,
    isUserEdited = isUserEdited,
)

internal fun Transaction.toEntity() = TransactionEntity(
    id = id,
    amount = amount,
    merchantRaw = merchantRaw,
    cardName = cardName,
    payType = payType,
    kind = kind.name,
    paidAt = paidAt,
    latitude = location?.latitude,
    longitude = location?.longitude,
    locationAccuracyM = locationAccuracyM,
    placeId = placeId,
    isOnline = isOnline,
    isUserEdited = isUserEdited,
)

internal fun PlaceEntity.toModel() = Place(
    id = id,
    name = name,
    latLng = LatLng(latitude, longitude),
    category = category,
    address = address,
)

internal fun Place.toEntity() = PlaceEntity(
    id = id,
    name = name,
    latitude = latLng.latitude,
    longitude = latLng.longitude,
    category = category,
    address = address,
)
