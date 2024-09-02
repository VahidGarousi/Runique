package ir.runique.run.presentation.run_overview

import ir.runique.run.presentation.run_overview.model.RunUi

data class RunOverviewState(
    val runs : List<RunUi> = emptyList(),
)
