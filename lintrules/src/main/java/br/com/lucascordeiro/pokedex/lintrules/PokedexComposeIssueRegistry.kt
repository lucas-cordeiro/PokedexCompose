package br.com.lucascordeiro.pokedex.lintrules

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

class PokedexComposeIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = emptyList()

    override val api: Int = CURRENT_API
}