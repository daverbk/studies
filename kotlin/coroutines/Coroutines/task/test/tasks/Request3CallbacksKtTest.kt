package tasks

import contributors.MockGithubService
import contributors.expectedResults
import contributors.testRequestData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class Request3CallbacksKtTest {
    @Test
    fun testDataIsLoaded() {
        loadContributorsCallbacks(MockGithubService, testRequestData) {
            Assert.assertEquals(
                "Wrong result for 'loadContributorsCallbacks'",
                expectedResults.users, it
            )
        }
    }
}