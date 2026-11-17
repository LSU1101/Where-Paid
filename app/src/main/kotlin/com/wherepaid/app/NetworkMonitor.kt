package com.wherepaid.app

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * 인터넷 연결 여부. 화면의 오프라인 안내(분석서 4-2)에 쓴다.
 * 매칭 대기 결제의 자동 재검색(②④)도 이 값을 쓸 수 있다.
 */
class NetworkMonitor(context: Context) {
    private val connectivity = context.applicationContext.getSystemService(ConnectivityManager::class.java)

    val isOnline: Flow<Boolean> = callbackFlow {
        // 기본 망의 상태는 콜백이 넘겨주는 값으로 판단한다. 끊긴 직후에 activeNetwork를 다시 물으면 아직 끊긴 망이 나온다.
        // VALIDATED(구글 서버 확인)가 아니라 INTERNET으로 본다. 검증 서버에 못 닿는 망(에뮬레이터, 일부 학교·회사 망)에서도
        // 오프라인으로 오판하지 않기 위해서다. 실제로 검색이 실패하면 그 결제는 매칭 대기가 된다.
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                trySend(capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
            }

            override fun onLost(network: Network) {
                trySend(false)
            }
        }
        val initial = connectivity.getNetworkCapabilities(connectivity.activeNetwork)
        trySend(initial?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true)
        // 기본 망이 생기면 onAvailable 뒤에 onCapabilitiesChanged가 항상 온다.
        connectivity.registerDefaultNetworkCallback(callback)
        awaitClose { connectivity.unregisterNetworkCallback(callback) }
    }.conflate().distinctUntilChanged()
}
