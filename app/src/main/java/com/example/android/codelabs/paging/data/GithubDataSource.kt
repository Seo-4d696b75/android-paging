package com.example.android.codelabs.paging.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android.codelabs.paging.api.GithubService
import com.example.android.codelabs.paging.api.IN_QUALIFIER
import com.example.android.codelabs.paging.model.Repo
import java.io.IOException

// GitHub page API is 1 based: https://developer.github.com/v3/#pagination
private const val GITHUB_STARTING_PAGE_INDEX = 1

class GithubDataSource(
    private val service: GithubService,
    private val query: String
    ) : PagingSource<Int, Repo>() {
    override fun getRefreshKey(state: PagingState<Int, Repo>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.let {closest ->
                closest.prevKey?.plus(1) ?: closest.nextKey?.minus(1)
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repo> {
        val page = params.key ?: GITHUB_STARTING_PAGE_INDEX
        return try {
            val res = service.searchRepos(
                query + IN_QUALIFIER, page, params.loadSize
            )
            val prePage = if (page == GITHUB_STARTING_PAGE_INDEX) null else
                page - 1
            val nextPage = if (res.items.isEmpty()) null else
                page + (params.loadSize / GithubRepository.NETWORK_PAGE_SIZE)
            LoadResult.Page(res.items, prePage, nextPage)
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }
}
