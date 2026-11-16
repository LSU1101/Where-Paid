package com.wherepaid.app.core.model.fake

import com.wherepaid.app.core.model.LocationFailure
import com.wherepaid.app.core.model.LocationResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class FakesTest {

    @Test
    fun `거래 목록은 최근 결제가 먼저 온다`() = runTest {
        val all = FakeTransactionRepository().observeAll().first()

        assertEquals(SampleData.transactions.size, all.size)
        assertEquals(all.sortedByDescending { it.paidAt }, all)
    }

    @Test
    fun `upsert는 같은 id를 덮어쓰고 delete는 지운다`() = runTest {
        val repository = FakeTransactionRepository()
        val original = SampleData.transactions.first()

        repository.upsert(original.copy(amount = 999, isUserEdited = true))
        assertEquals(999L, repository.get(original.id)?.amount)
        assertEquals(SampleData.transactions.size, repository.observeAll().first().size)

        repository.delete(original.id)
        assertNull(repository.get(original.id))
    }

    @Test
    fun `샘플 거래의 placeId는 모두 샘플 장소에 있다`() = runTest {
        val repository = FakeTransactionRepository()

        SampleData.transactions.mapNotNull { it.placeId }.forEach { placeId ->
            assertTrue(placeId, repository.getPlace(placeId) != null)
        }
    }

    @Test
    fun `장소 찾기는 이름이 맞으면 장소를, 아니면 null을 준다`() = runTest {
        val resolver = FakePlaceResolver()

        assertEquals("fake-place-2", resolver.resolve("샘플분식", SampleData.CITY_HALL)?.id)
        assertNull(resolver.resolve("없는가게", null))
    }

    @Test
    fun `위치 제공자는 설정한 결과를 돌려준다`() = runTest {
        val provider = FakeLocationProvider()
        assertTrue(provider.capture() is LocationResult.Success)

        provider.result = LocationResult.Failure(LocationFailure.TIMEOUT)
        assertEquals(LocationResult.Failure(LocationFailure.TIMEOUT), provider.capture())
    }
}
