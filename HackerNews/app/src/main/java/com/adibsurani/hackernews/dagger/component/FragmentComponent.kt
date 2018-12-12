package com.adibsurani.hackernews.dagger.component

import com.adibsurani.hackernews.dagger.module.FragmentModule
import com.adibsurani.hackernews.ui.fragment.CommentFragment
import dagger.Component

@Component(modules = [FragmentModule::class])
interface FragmentComponent {
    fun inject(commentFragment: CommentFragment)
}