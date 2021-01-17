package life.league.challenge.kotlin.session

import org.junit.Assert
import org.junit.Test

class DefaultSessionManagerTest {

    @Test
    fun `purge clear apiKey`() {
        val manager = DefaultSessionManager().apply {
            _apiKey = "Real api key"
        }
        manager.purge()
        Assert.assertTrue(manager._apiKey == null)
    }

    @Test
    fun `setApiKey sets the value correctly`() {
        val key = "Real api key"
        val manager = DefaultSessionManager().apply {
            setApiKey(key)
        }
        Assert.assertTrue(manager._apiKey.equals(key))
    }

    @Test
    fun `getApiKey return the correct value`() {
        val key = "Real api key"
        val manager = DefaultSessionManager().apply {
            _apiKey = key
        }
        Assert.assertTrue(manager.getApiKey().equals(key))
    }
}