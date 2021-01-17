package life.league.challenge.kotlin.ui.homescreen

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import life.league.challenge.kotlin.data.network.endpoint.base.ApiError
import life.league.challenge.kotlin.data.network.model.PostApiModel
import life.league.challenge.kotlin.data.network.model.UserApiModel
import life.league.challenge.kotlin.ui.homescreen.usecase.feed.getFeedFailureReason
import life.league.challenge.kotlin.ui.homescreen.usecase.feed.toFeedItems
import life.league.challenge.kotlin.ui.homescreen.usecase.feed.toUiModel
import life.league.challenge.kotlin.ui.homescreen.usecase.feed.toUiModels
import life.league.challenge.kotlin.ui.homescreen.usecase.login.FailureReason
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DefaultFeedUseCaseTest {

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ApiError_getFeedFailureReason() returns correct type`() {
        Assert.assertTrue(ApiError.Unreachable(null).getFeedFailureReason() == FailureReason.NetworkIssue)
        Assert.assertTrue(ApiError.ClientError.Unauthorised(null).getFeedFailureReason() == FailureReason.InvalidCredentials)
        Assert.assertTrue(ApiError.GenericError(message = null).getFeedFailureReason() == FailureReason.GenericIssue)
    }

    @Test
    fun `UserApiModel_toUiModel maps necessary values correctly`() {
        val apiModel = getUserApiModel(7)
        val uiModel = apiModel.toUiModel()
        Assert.assertTrue(uiModel.userId == apiModel.id)
        Assert.assertTrue(uiModel.userName == apiModel.username)
        Assert.assertTrue(uiModel.avatar == apiModel.avatar)
        Assert.assertTrue(uiModel.phone == apiModel.phone)
        Assert.assertTrue(uiModel.email == apiModel.email)
        Assert.assertTrue(uiModel.website == apiModel.website)
    }

    @Test
    fun `PostApiModel_toUiModel maps value correctly`() {
        val apiPost = PostApiModel(
                id = 72L,
                title = "Post title",
                body = "Post body",
                userId = 9L
        )
        val uiModel = apiPost.toUiModel()
        Assert.assertTrue(uiModel.postId == apiPost.id)
        Assert.assertTrue(uiModel.title == apiPost.title)
        Assert.assertTrue(uiModel.description == apiPost.body)
    }

    @Test
    fun `List(UserApiMode)_toUiModels maps ids correctly`() {
        val apiModel0 = getUserApiModel(0)
        val apiModel1 = getUserApiModel(1)
        val apiModel2 = getUserApiModel(2)
        val apiModel3 = getUserApiModel(3)
        val apiModel4 = getUserApiModel(4)
        val apiModel5 = getUserApiModel(5)
        val apiModel6 = getUserApiModel(6)
        val uiModelsMap = runBlocking {
            listOf(apiModel0, apiModel1, apiModel2, apiModel3, apiModel4, apiModel5, apiModel6).toUiModels()
        }
        assert(uiModelsMap.size == 7)
        assert(uiModelsMap[apiModel0.id]?.name == apiModel0.name)
        assert(uiModelsMap[apiModel1.id]?.name == apiModel1.name)
        assert(uiModelsMap[apiModel2.id]?.name == apiModel2.name)
        assert(uiModelsMap[apiModel3.id]?.name == apiModel3.name)
        assert(uiModelsMap[apiModel4.id]?.name == apiModel4.name)
        assert(uiModelsMap[apiModel5.id]?.name == apiModel5.name)
        assert(uiModelsMap[apiModel6.id]?.name == apiModel6.name)
    }

    @Test
    fun `List(PostApiModel)_toFeedItems(users) returns feeds items only for posts with a matching user and maintains index order`() {
        val user1 = getUserApiModel(1)
        val user2 = getUserApiModel(2)
        val user3 = getUserApiModel(3)
        val apiUsers = runBlocking { listOf(user1, user2, user3).toUiModels() }

        val post1 = getPostApiModel(1).copy(userId = user1.id)
        val post2 = getPostApiModel(2).copy(userId = user2.id)
        val post3 = getPostApiModel(3).copy(userId = user1.id)
        val post4 = getPostApiModel(4) // no user
        val post5 = getPostApiModel(5).copy(userId = user1.id)
        val post6 = getPostApiModel(6).copy(userId = user3.id)
        val post7 = getPostApiModel(7) // no user
        val post8 = getPostApiModel(8) // no user

        val apiPosts = listOf(post1, post2, post3, post4, post5, post6, post7, post8)

        val feed = runBlocking { apiPosts.toFeedItems(apiUsers) }

        assert(feed.size == 5)
        assert(feed[0].post.postId == post1.id)
        assert(feed[1].post.postId == post2.id)
        assert(feed[2].post.postId == post3.id)
        assert(feed[3].post.postId == post5.id)
        assert(feed[4].post.postId == post6.id)

    }


    /**
     * Helper function for testing.
     * @return PostApiModel
     * @param postFix value appended to end all properties.
     * */
    private fun getPostApiModel(postFix: Int): PostApiModel {
        return PostApiModel(
                id = 100L + postFix,
                title = "Post title$postFix",
                body = "Post body$postFix",
                userId = USER_ID_DEFAULT_VALUE + postFix
        )
    }


    /**
     * Helper function for testing.
     * @return UserApiModel
     * @param postFix value appended to end all properties.
     * */
    private fun getUserApiModel(postFix: Int): UserApiModel {
        return UserApiModel(
                id = USER_ID_DEFAULT_VALUE + postFix,
                username = "my username$postFix",
                name = "my name$postFix",
                avatar = "my avatar$postFix",
                email = "my email$postFix",
                phone = "my phone$postFix",
                website = "my website$postFix",
                address = Mockito.mock(UserApiModel.Address::class.java),
                company = Mockito.mock(UserApiModel.Company::class.java)
        )
    }

}

private const val USER_ID_DEFAULT_VALUE = 0L
