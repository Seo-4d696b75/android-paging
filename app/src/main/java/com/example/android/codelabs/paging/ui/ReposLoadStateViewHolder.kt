package com.example.android.codelabs.paging.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.codelabs.paging.databinding.ViewReposLoadStateFooterBinding

class ReposLoadStateViewHolder(
    private val binding: ViewReposLoadStateFooterBinding,
    retryCallback: () -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun create(parent: ViewGroup, callback: () -> Unit):
            ReposLoadStateViewHolder {
            val binding = ViewReposLoadStateFooterBinding.inflate(
                LayoutInflater.from(parent.context)
            )
            return ReposLoadStateViewHolder(binding, callback)
        }
    }

    init {
        binding.buttonReload.setOnClickListener { retryCallback.invoke() }
    }

    fun bind(state: LoadState) {
        if (state is LoadState.Error) {
            binding.textErrorMes.text = state.error.localizedMessage
        }
        binding.progressLoading.isVisible = state is LoadState.Loading
        binding.buttonReload.isVisible = state is LoadState.Error
        binding.textErrorMes.isVisible = state is LoadState.Error
    }
}

class ReposLoadStateAdapter(
    private val callback: () -> Unit
): LoadStateAdapter<ReposLoadStateViewHolder>() {
    override fun onBindViewHolder(
        holder: ReposLoadStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ReposLoadStateViewHolder {
        return ReposLoadStateViewHolder.create(parent, callback)
    }
}